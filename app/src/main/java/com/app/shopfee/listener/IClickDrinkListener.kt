package com.app.shopfee.listener

import com.app.shopfee.model.Drink

interface IClickDrinkListener {
    fun onClickDrinkItem(drink: Drink)
}