package com.app.shopfee.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.shopfee.MyApplication
import com.app.shopfee.R
import com.app.shopfee.database.DrinkDatabase
import com.app.shopfee.event.DisplayCartEvent
import com.app.shopfee.event.OrderSuccessEvent
import com.app.shopfee.model.Order
import com.app.shopfee.utils.Constant
import com.app.shopfee.utils.GlobalFunction
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import org.greenrobot.eventbus.EventBus

class PaymentActivity : BaseActivity() {

    private var mOrderBooking: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        getDataIntent()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ createOrderFirebase() }, 2000)
    }

    private fun getDataIntent() {
        val bundle = intent.extras ?: return
        mOrderBooking = bundle[Constant.ORDER_OBJECT] as Order?
    }

    private fun createOrderFirebase() {
        MyApplication[this].getOrderDatabaseReference()
            ?.child(mOrderBooking?.id.toString())
            ?.setValue(
                mOrderBooking
            ) { _: DatabaseError?, _: DatabaseReference? ->
                DrinkDatabase.getInstance(this)!!
                    .drinkDAO().deleteAllDrink()
                EventBus.getDefault().post(DisplayCartEvent())
                EventBus.getDefault().post(OrderSuccessEvent())
                val bundle = Bundle()
                bundle.putLong(Constant.ORDER_ID, mOrderBooking?.id!!)
                GlobalFunction.startActivity(
                    this@PaymentActivity,
                    ReceiptOrderActivity::class.java, bundle
                )
                finish()
            }
    }
}