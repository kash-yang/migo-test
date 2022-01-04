package tv.migo.test.utils

import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Number?.toThousandsFormat(scale: Int,
                              zeroFill: Boolean = true,
                              failureDisplay: String = "--",
                              roundMode: RoundingMode = RoundingMode.HALF_UP,
                              max: Double? = null, min: Double? = null,
                              processValue: ((Double) -> Double)? = null): String {

    if (this == null) return failureDisplay

    val scalePatternBuilder = StringBuilder()
    val floatPattern = if (zeroFill) {
        "0"
    } else {
        "#"
    }
    val thousandsFormat = DecimalFormat(if (scale > 0) {
        for (i in 0 until scale) {
            scalePatternBuilder.append(floatPattern)
        }
        "#,##0.$scalePatternBuilder"
    } else {
        "#,###"
    })
    thousandsFormat.roundingMode = roundMode

    val finalValue = processValue?.invoke(this.toDouble()) ?: this.toDouble()
    return thousandsFormat.format(
        if (max != null && finalValue > max) {
            max
        } else if (min != null && finalValue < min) {
            min
        } else {
            finalValue
        }
    )
}

fun Long.toTimeFormat(pattern: String = "yyyy-MM-dd HH:mm:ss:SSS EEEE", locale: Locale? = Locale.getDefault()): String {
    val formatter: DateFormat = SimpleDateFormat(pattern, locale)
    return formatter.format(this)
}