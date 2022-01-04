package tv.migo.test.db.convert

import androidx.room.TypeConverter
import tv.migo.test.db.data.PassType

class PassTypeConvert {
    @TypeConverter
    fun toPassType(raw: String) = PassType.valueOf(raw)

    @TypeConverter
    fun fromPassType(type: PassType) = type.name
}