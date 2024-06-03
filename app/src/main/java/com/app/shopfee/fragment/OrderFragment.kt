package com.app.shopfee.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.MyApplication
import com.app.shopfee.R
import com.app.shopfee.activity.ReceiptOrderActivity
import com.app.shopfee.activity.TrackingOrderActivity
import com.app.shopfee.adapter.OrderAdapter
import com.app.shopfee.adapter.OrderAdapter.IClickOrderListener
import com.app.shopfee.model.Order
import com.app.shopfee.model.TabOrder
import com.app.shopfee.prefs.DataStoreManager
import com.app.shopfee.utils.Constant
import com.app.shopfee.utils.GlobalFunction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OrderFragment : Fragment() {

    private var mView: View? = null
    private var orderTabType = 0
    private var listOrder: MutableList<Order>? = null
    private var orderAdapter: OrderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_order, container, false)
        getDataArguments()
        initUi()
        getListOrderFromFirebase()
        return mView
    }

    private fun getDataArguments() {
        val bundle = arguments ?: return
        orderTabType = bundle.getInt(Constant.ORDER_TAB_TYPE)
    }

    private fun initUi() {
        listOrder = ArrayList()
        val rcvOrder = mView?.findViewById<RecyclerView>(R.id.rcv_order)
        val linearLayoutManager = LinearLayoutManager(activity)
        rcvOrder?.layoutManager = linearLayoutManager
        orderAdapter = OrderAdapter(activity, listOrder, object : IClickOrderListener {
            override fun onClickTrackingOrder(orderId: Long) {
                val bundle = Bundle()
                bundle.putLong(Constant.ORDER_ID, orderId)
                GlobalFunction.startActivity(activity, TrackingOrderActivity::class.java, bundle)
            }

            override fun onClickReceiptOrder(order: Order) {
                val bundle = Bundle()
                bundle.putLong(Constant.ORDER_ID, order.id)
                GlobalFunction.startActivity(activity, ReceiptOrderActivity::class.java, bundle)
            }
        })
        rcvOrder?.adapter = orderAdapter
    }

    private fun getListOrderFromFirebase() {
        if (activity == null) return
        MyApplication[activity].getOrderDatabaseReference()
            ?.orderByChild("userEmail")
            ?.equalTo(DataStoreManager.user?.email)
            ?.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (listOrder != null) {
                        listOrder!!.clear()
                    } else {
                        listOrder = ArrayList()
                    }
                    for (dataSnapshot in snapshot.children) {
                        val order = dataSnapshot.getValue(
                            Order::class.java
                        )
                        if (order != null) {
                            if (TabOrder.TAB_ORDER_PROCESS == orderTabType) {
                                if (Order.STATUS_COMPLETE != order.status) {
                                    listOrder!!.add(0, order)
                                }
                            } else if (TabOrder.TAB_ORDER_DONE == orderTabType) {
                                if (Order.STATUS_COMPLETE == order.status) {
                                    listOrder!!.add(0, order)
                                }
                            }
                        }
                    }
                    if (orderAdapter != null) orderAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (orderAdapter != null) orderAdapter!!.release()
    }

    companion object {
        fun newInstance(type: Int): OrderFragment {
            val orderFragment = OrderFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.ORDER_TAB_TYPE, type)
            orderFragment.arguments = bundle
            return orderFragment
        }
    }
}