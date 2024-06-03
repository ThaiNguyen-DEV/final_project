package com.app.shopfee.model

import com.app.shopfee.utils.StringUtil
import java.io.Serializable

class Order : Serializable {
    var id: Long = 0
    var userEmail: String? = null
    var dateTime: String? = null
    var drinks: List<DrinkOrder?>? = null
    var price = 0
    var voucher = 0
    var total = 0
    var paymentMethod: String? = null
    var status = 0
    var rate = 0.0
    var review: String? = null
    var address: Address? = null

    fun getListDrinksName(): String {
        if (drinks == null || drinks!!.isEmpty()) return ""
        var result = ""
        for (drinkOrder in drinks!!) {
            result += if (StringUtil.isEmpty(result)) {
                drinkOrder?.name
            } else {
                ", " + drinkOrder?.name
            }
        }
        return result
    }

    companion object {
        const val STATUS_NEW = 1
        const val STATUS_DOING = 2
        const val STATUS_ARRIVED = 3
        const val STATUS_COMPLETE = 4
    }
}