package tv.migo.test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import tv.migo.test.R
import tv.migo.test.databinding.FragmentDetailBinding
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.isExpired
import tv.migo.test.utils.toTimeFormat

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    val args by navArgs<DetailFragmentArgs>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.type.text = String.format(getString(R.string.detail_type), args.passInfo.passType.raw)
        binding.length.text = String.format(getString(R.string.detail_length), args.passInfo.passLength.toString())
        binding.price.text = String.format(getString(R.string.detail_price), args.passInfo.price.toString())
        binding.generateAt.text = String.format(getString(R.string.detail_generate_at), args.passInfo.generateAt.toTimeFormat())
        binding.activeAt.text = String.format(getString(R.string.detail_active_at), args.passInfo.activeAt?.toTimeFormat())
        val expiredAt = args.passInfo.expiredAt?.let {
            val ret = when(args.passInfo.passType) {
                PassType.DAY ->  it + 1
                else -> it
            }
            ret
        }
        binding.expiredAt.text = String.format(getString(R.string.detail_expired_at), expiredAt?.toTimeFormat())
        binding.isExpired.text = String.format(getString(R.string.detail_expired), args.passInfo.isExpired())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}