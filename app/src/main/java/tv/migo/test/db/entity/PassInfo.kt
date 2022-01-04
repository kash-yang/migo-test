package tv.migo.test.db.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import tv.migo.test.db.data.PassType

@Parcelize
@Entity(tableName = "pass", primaryKeys= [ "pass_type", "pass_length" ])
data class PassInfo(

    @ColumnInfo(name = "pass_type")
    @NonNull
    val passType: PassType,

    @ColumnInfo(name = "pass_length")
    @NonNull
    val passLength: Int,

    @ColumnInfo(name = "price")
    @NonNull
    val price: Int,

    @ColumnInfo(name = "generate_at")
    @NonNull
    val generateAt: Long,

    @ColumnInfo(name = "active_at")
    @Nullable
    val activeAt: Long? = null,

    @ColumnInfo(name = "expired_at")
    @Nullable
    val expiredAt: Long? = null
) : Parcelable

fun PassInfo.isExpired() = System.currentTimeMillis() > (expiredAt ?: Long.MAX_VALUE)