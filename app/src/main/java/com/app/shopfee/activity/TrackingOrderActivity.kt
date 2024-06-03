package com.app.shopfee.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.MyApplication
import com.app.shopfee.R
import com.app.shopfee.adapter.DrinkOrderAdapter
import com.app.shopfee.model.Order
import com.app.shopfee.model.RatingReview
import com.app.shopfee.utils.Constant
import com.app.shopfee.utils.GlobalFunction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class TrackingOrderActivity : BaseActivity() {

    private var rcvDrinks: RecyclerView? = null
    private var layoutReceiptOrder: LinearLayout? = null
    private var dividerStep1: View? = null
    private var dividerStep2: View? = null
    private var imgStep1: ImageView? = null
    private var imgStep2: ImageView? = null
    private var imgStep3: ImageView? = null
    private var tvTakeOrder: TextView? = null
    private var tvTakeOrderMessage: TextView? = null
    private var orderId: Long = 0
    private var mOrder: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_order)

        getDataIntent()
        initToolbar()
        initUi()
        initListener()
        getOrderDetailFromFirebase()
    }

    private fun getDataIntent() {
        val bundle = intent.extras ?: return
        orderId = bundle.getLong(Constant.ORDER_ID)
    }

    private fun initToolbar() {
        val imgToolbarBack = findViewById<ImageView>(R.id.img_toolbar_back)
        val tvToolbarTitle = findViewById<TextView>(R.id.tv_toolbar_title)
        imgToolbarBack.setOnClickListener { finish() }
        tvToolbarTitle.text = getString(R.string.label_tracking_order)
    }

    private fun initUi() {
        rcvDrinks = findViewById(R.id.rcv_drinks)
        val linearLayoutManager = LinearLayoutManager(this)
        rcvDrinks?.layoutManager = linearLayoutManager
        layoutReceiptOrder = findViewById(R.id.layout_receipt_order)
        dividerStep1 = findViewById(R.id.divider_step_1)
        dividerStep2 = findViewById(R.id.divider_step_2)
        imgStep1 = findViewById(R.id.img_step_1)
        imgStep2 = findViewById(R.id.img_step_2)
        imgStep3 = findViewById(R.id.img_step_3)
        tvTakeOrder = findViewById(R.id.tv_take_order)
        tvTakeOrderMessage = findViewById(R.id.tv_take_order_message)
    }

    private fun initListener() {
        layoutReceiptOrder!!.setOnClickListener {
            if (mOrder == null) return@setOnClickListener
            val bundle = Bundle()
            bundle.putLong(Constant.ORDER_ID, mOrder?.id!!)
            GlobalFunction.startActivity(
                this@TrackingOrderActivity,
                ReceiptOrderActivity::class.java, bundle
            )
            finish()
        }
        imgStep1!!.setOnClickListener { updateStatusOrder(Order.STATUS_NEW) }
        imgStep2!!.setOnClickListener { updateStatusOrder(Order.STATUS_DOING) }
        imgStep3!!.setOnClickListener { updateStatusOrder(Order.STATUS_ARRIVED) }
        tvTakeOrder!!.setOnClickListener { updateStatusOrder(Order.STATUS_COMPLETE) }
    }

    private fun getOrderDetailFromFirebase() {
        showProgressDialog(true)
        MyApplication[this].getOrderDetailDatabaseReference(orderId)
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    showProgressDialog(false)
                    mOrder = snapshot.getValue(Order::class.java)
                    if (mOrder == null) return
                    initData()
                }

                override fun onCancelled(error: DatabaseError) {
                    showProgressDialog(false)
                    showToastMessage(getString(R.string.msg_get_date_error))
                }
            })
    }

    private fun initData() {
        val adapter = DrinkOrderAdapter(mOrder?.drinks)
        rcvDrinks!!.adapter = adapter
        when (mOrder?.status) {
            Order.STATUS_NEW -> {
                imgStep1!!.setImageResource(R.drawable.ic_step_enable)
                dividerStep1!!.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                imgStep2!!.setImageResource(R.drawable.ic_step_disable)
                dividerStep2!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                imgStep3!!.setImageResource(R.drawable.ic_step_disable)
                tvTakeOrder!!.setBackgroundResource(R.drawable.bg_button_disable_corner_16)
                tvTakeOrderMessage!!.visibility = View.GONE
            }
            Order.STATUS_DOING -> {
                imgStep1!!.setImageResource(R.drawable.ic_step_enable)
                dividerStep1!!.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                imgStep2!!.setImageResource(R.drawable.ic_step_enable)
                dividerStep2!!.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                imgStep3!!.setImageResource(R.drawable.ic_step_disable)
                tvTakeOrder!!.setBackgroundResource(R.drawable.bg_button_disable_corner_16)
                tvTakeOrderMessage!!.visibility = View.GONE
            }
            Order.STATUS_ARRIVED -> {
                imgStep1!!.setImageResource(R.drawable.ic_step_enable)
                dividerStep1!!.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                imgStep2!!.setImageResource(R.drawable.ic_step_enable)
                dividerStep2!!.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                imgStep3!!.setImageResource(R.drawable.ic_step_enable)
                tvTakeOrder!!.setBackgroundResource(R.drawable.bg_button_enable_corner_16)
                tvTakeOrderMessage!!.visibility = View.VISIBLE
            }
        }
    }

    private fun updateStatusOrder(status: Int) {
        if (mOrder == null) return
        val map: MutableMap<String, Any> = HashMap()
        map["status"] = status
        MyApplication[this].getOrderDatabaseReference()
            ?.child(mOrder?.id.toString())
            ?.updateChildren(
                map
            ) { _: DatabaseError?, _: DatabaseReference? ->
                if (Order.STATUS_COMPLETE == status) {
                    val bundle = Bundle()
                    val ratingReview = RatingReview(
                        RatingReview.TYPE_RATING_REVIEW_ORDER,
                        mOrder?.id.toString()
                    )
                    bundle.putSerializable(Constant.RATING_REVIEW_OBJECT, ratingReview)
                    GlobalFunction.startActivity(
                        this@TrackingOrderActivity,
                        RatingReviewActivity::class.java, bundle
                    )
                    finish()
                }
            }
    }
}