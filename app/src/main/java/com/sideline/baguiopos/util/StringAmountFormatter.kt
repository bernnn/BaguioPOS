package com.sideline.baguiopos.util

import java.text.DecimalFormat

class StringAmountFormatter{

    companion object {
        fun amountFormat(value: String): String {
            val pattern = "#,###,###.00"
            val doubleValue = value.toDouble()
            val decimalFormat = DecimalFormat(pattern)
            decimalFormat.applyPattern(pattern)
            return decimalFormat.format(doubleValue)
        }
    }

}