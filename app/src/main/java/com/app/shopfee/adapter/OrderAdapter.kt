package com.app.shopfee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.R
import com.app.shopfee.adapter.OrderAdapter.OrderViewHolder
import com.app.shopfee.model.Order
import com.app.shopfee.utils.Constant
import com.app.shopfee.utils.GlideUtils

class OrderAdapter(
    private var context: Context?,
    private val listOrder: List<Order>?,
    private val iClickOrderListener: IClickOrderListener
) : RecyclerView.Adapter<OrderViewHolder>() {

    interface IClickOrderListener {
        fun onClickTrackingOrder(orderId: Long)
        fun onClickReceiptOrder(order: Order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = listOrder!![position]
        val firstDrinkOrder = order.drinks?.get(0)
        GlideUtils.loadUrl(firstDrinkOrder?.image, holder.imgDrink)
        holder.tvOrderId.text = order.id.toString()
        val strTotal = order.total.toString() + Constant.CURRENCY
        holder.tvTotal.text = strTotal
        holder.tvDrinksName.text = order.getListDrinksName()
        val strQuantity =
            "(" + order.drinks?.size + " " + context!!.getString(R.string.label_item) + ")"
        holder.tvQuantity.text = strQuantity
        if (Order.STATUS_COMPLETE == order.status) {
            holder.tvSuccess.visibility = View.VISIBLE
            holder.tvAction.text = context!!.getString(R.string.label_receipt_order)
            holder.layoutReview.visibility = View.VISIBLE
            holder.tvRate.text = order.rate.toString()
            holder.tvReview.text = order.review
            holder.layoutAction.setOnClickListener {
                iClickOrderListener.onClickReceiptOrder(order)
            }
        } else {
            holder.tvSuccess.visibility = View.GONE
            holder.tvAction.text = context!!.getString(R.string.label_tracking_order)
            holder.layoutReview.visibility = View.GONE
            holder.layoutAction.setOnClickListener {
                iClickOrderListener.onClickTrackingOrder(order.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOrder?.size ?: 0
    }

    fun release() {
        if (context != null) context = null
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDrink: ImageView
        val tvOrderId: TextView
        val tvTotal: TextView
        val tvDrinksName: TextView
        val tvQuantity: TextView
        val tvSuccess: TextView
        val layoutAction: LinearLayout
        val tvAction: TextView
        val layoutReview: LinearLayout
        val tvRate: TextView
        val tvReview: TextView

        init {
            imgDrink = itemView.findViewById(R.id.img_drink)
            tvOrderId = itemView.findViewById(R.id.tv_order_id)
            tvTotal = itemView.findViewById(R.id.tv_total)
            tvDrinksName = itemView.findViewById(R.id.tv_drinks_name)
            tvQuantity = itemView.findViewById(R.id.tv_quantity)
            tvSuccess = itemView.findViewById(R.id.tv_success)
            layoutAction = itemView.findViewById(R.id.layout_action)
            tvAction = itemView.findViewById(R.id.tv_action)
            layoutReview = itemView.findViewById(R.id.layout_review)
            tvRate = itemView.findViewById(R.id.tv_rate)
            tvReview = itemView.findViewById(R.id.tv_review)
        }
    }
}