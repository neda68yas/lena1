package com.example.ui.components

fun String.toPersianDigits(): String {
    var result = this
    val englishDigits = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val persianDigits = arrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    for (i in englishDigits.indices) {
        result = result.replace(englishDigits[i], persianDigits[i])
    }
    return result
}

fun Int.toPersianDigits(): String = this.toString().toPersianDigits()

fun Float.toPersianDigits(precision: Int = 1): String {
    val formatted = String.format("%.${precision}f", this)
    return formatted.toPersianDigits()
}

fun Double.toPersianDigits(precision: Int = 1): String {
    val formatted = String.format("%.${precision}f", this)
    return formatted.toPersianDigits()
}
