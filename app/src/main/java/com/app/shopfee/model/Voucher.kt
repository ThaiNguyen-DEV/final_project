package com.app.shopfee.model

import com.app.shopfee.utils.Constant
import java.io.Serializable

class Voucher : Serializable {
    var id = 0
    var discount = 0
    var minimum = 0
    var isSelected = false
    val title: String
        get() = "Discount $discount%"
    val minimumText: String
        get() = if (minimum > 0) {
            "Apply for minimum order " + minimum + Constant.CURRENCY
        } else "Apply for all order"

    fun getCondition(amount: Int): String {
        if (minimum <= 0) return ""
        val condition = minimum - amount
        return if (condition > 0) {
            "Please buy more " + condition + Constant.CURRENCY + " to use this discount"
        } else ""
    }

    fun isVoucherEnable(amount: Int): Boolean {
        if (minimum <= 0) return true
        val condition = minimum - amount
        return condition <= 0
    }

    fun getPriceDiscount(amount: Int): Int {
        return amount * discount / 100
    }
}