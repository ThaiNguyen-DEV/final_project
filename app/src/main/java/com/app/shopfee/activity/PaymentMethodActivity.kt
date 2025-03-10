package com.app.shopfee.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.MyApplication
import com.app.shopfee.R
import com.app.shopfee.adapter.PaymentMethodAdapter
import com.app.shopfee.event.PaymentMethodSelectedEvent
import com.app.shopfee.model.PaymentMethod
import com.app.shopfee.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class PaymentMethodActivity : BaseActivity() {

    private var rcvPaymentMethod: RecyclerView? = null
    private var listPaymentMethod: MutableList<PaymentMethod>? = null
    private var paymentMethodAdapter: PaymentMethodAdapter? = null
    private var paymentMethodSelectedId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        getDataIntent()
        initToolbar()
        initUi()
        getListPaymentMethodFromFirebase()
    }

    private fun getDataIntent() {
        val bundle = intent.extras ?: return
        paymentMethodSelectedId = bundle.getInt(Constant.PAYMENT_METHOD_ID, 0)
    }

    private fun initUi() {
        rcvPaymentMethod = findViewById(R.id.rcv_payment_method)
        val linearLayoutManager = LinearLayoutManager(this)
        rcvPaymentMethod?.layoutManager = linearLayoutManager

        listPaymentMethod = ArrayList()
        paymentMethodAdapter =
                PaymentMethodAdapter(listPaymentMethod, object : PaymentMethodAdapter.IClickPaymentMethodListener {
                    override fun onClickPaymentMethodItem(paymentMethod: PaymentMethod) {
                        handleClickPaymentMethod(paymentMethod)
                    }
                })
        rcvPaymentMethod?.adapter = paymentMethodAdapter
    }

    private fun initToolbar() {
        val imgToolbarBack = findViewById<ImageView>(R.id.img_toolbar_back)
        val tvToolbarTitle = findViewById<TextView>(R.id.tv_toolbar_title)
        imgToolbarBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = getString(R.string.title_payment_method)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListPaymentMethodFromFirebase() {
        showProgressDialog(true)
        MyApplication[this].getPaymentMethodDatabaseReference()
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    showProgressDialog(false)

                    resetListPaymentMethod()
                    for (dataSnapshot in snapshot.children) {
                        val paymentMethod = dataSnapshot.getValue(
                            PaymentMethod::class.java
                        )
                        if (paymentMethod != null) {
                            listPaymentMethod!!.add(paymentMethod)
                        }
                    }

                    if (paymentMethodSelectedId > 0 && listPaymentMethod != null
                            && listPaymentMethod!!.isNotEmpty()) {
                        for (paymentMethod in listPaymentMethod!!) {
                            if (paymentMethod.id == paymentMethodSelectedId) {
                                paymentMethod.isSelected = true
                                break
                            }
                        }
                    }
                    paymentMethodAdapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    showProgressDialog(false)
                    showToastMessage(getString(R.string.msg_get_date_error))
                }
            })
    }

    private fun resetListPaymentMethod() {
        if (listPaymentMethod != null) {
            listPaymentMethod!!.clear()
        } else {
            listPaymentMethod = ArrayList()
        }
    }

    private fun handleClickPaymentMethod(paymentMethod: PaymentMethod) {
        EventBus.getDefault().post(PaymentMethodSelectedEvent(paymentMethod))
        finish()
    }
}