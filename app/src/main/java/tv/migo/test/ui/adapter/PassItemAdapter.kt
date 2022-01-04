package tv.migo.test.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import tv.migo.test.R
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo
import tv.migo.test.utils.toThousandsFormat

class PassItemAdapter constructor(val context: Context, private val listener: PassItemClickListener?) : ListAdapter<PassInfoItem, ViewHolder>(DiffCallback()) {

    enum class PassItemType {
        DAY_HEADER, ITEM, HOUR_HEADER
    }

    @Synchronized fun update(input: List<PassInfo>) {
        val items = mutableListOf<PassInfoItem>()
        input.forEach {
            val type = when (val last = items.lastOrNull()) {
                null -> it.passType.toPassItemType()
                else -> {
                    if(last.passInfo?.passType != it.passType) {
                        it.passType.toPassItemType()
                    } else {
                        PassItemType.ITEM
                    }
                }
            }
            if(type == PassItemType.HOUR_HEADER || type == PassItemType.DAY_HEADER) {
                items.add(PassInfoItem(null, type))
            }
            items.add(PassInfoItem(it, PassItemType.ITEM))
        }
        submitList(items)
    }

    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return when (viewType) {
            PassItemType.DAY_HEADER.ordinal, PassItemType.HOUR_HEADER.ordinal -> {
                val view: View = mInflater.inflate(R.layout.pass_list_item_header, parent, false)
                HeaderViewHolder(view)
            }
            PassItemType.ITEM.ordinal -> {
                val view: View = mInflater.inflate(R.layout.pass_list_item, parent, false)
                ItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            item.passInfo?.let { info ->
                listener?.onPassItemClicked(info)
            }
        }
        when(holder) {
            is HeaderViewHolder -> {
                when (item.type) {
                    PassItemType.DAY_HEADER -> holder.primaryText.setText(R.string.header_day)
                    PassItemType.HOUR_HEADER -> holder.primaryText.setText(R.string.header_hour)
                    else -> {}
                }
            }
            is ItemViewHolder -> {
                when(item.passInfo?.passType) {
                    PassType.HOUR ->
                        holder.primaryText.text = String.format(context.getString(R.string.item_hour), item.passInfo.passLength.toString())
                    PassType.DAY ->
                        holder.primaryText.text = String.format(context.getString(R.string.item_day), item.passInfo.passLength.toString())
                    else -> {}
                }
                holder.secondaryText.text =
                    String.format(
                        context.getString(R.string.item_secondary),
                        item.passInfo?.price.toThousandsFormat(0).replace(",", "."))

                holder.buyPassButton.setOnClickListener {
                    item.passInfo?.let { info ->
                        listener?.onBuyPassClicked(info)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }
}

class DiffCallback : DiffUtil.ItemCallback<PassInfoItem>() {
    override fun areItemsTheSame(oldItem: PassInfoItem, newItem: PassInfoItem): Boolean {
        return oldItem.type == newItem.type &&
                oldItem.passInfo?.passType == newItem.passInfo?.passType &&
                oldItem.passInfo?.passLength == newItem.passInfo?.passLength
    }
    override fun areContentsTheSame(oldItem: PassInfoItem, newItem: PassInfoItem): Boolean {
        return oldItem == newItem
    }
}

class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
    val primaryText: TextView = itemView.findViewById(R.id.primary_text)
    val secondaryText: TextView = itemView.findViewById(R.id.secondary_text)
    val buyPassButton: Button = itemView.findViewById(R.id.buy_button)
}

class HeaderViewHolder(itemView: View) : ViewHolder(itemView) {
    val primaryText: TextView = itemView.findViewById(R.id.primary_text)
}

interface PassItemClickListener {
    fun onPassItemClicked(passInfo: PassInfo)
    fun onBuyPassClicked(passInfo: PassInfo)
}

data class PassInfoItem(val passInfo: PassInfo?, val type: PassItemAdapter.PassItemType)

fun PassType.toPassItemType() = when(this) {
    PassType.DAY -> PassItemAdapter.PassItemType.DAY_HEADER
    PassType.HOUR -> PassItemAdapter.PassItemType.HOUR_HEADER
}
