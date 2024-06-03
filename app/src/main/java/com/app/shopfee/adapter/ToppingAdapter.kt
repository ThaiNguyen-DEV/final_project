package com.app.shopfee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.R
import com.app.shopfee.adapter.ToppingAdapter.ToppingViewHolder
import com.app.shopfee.model.Topping
import com.app.shopfee.utils.Constant

class ToppingAdapter(
    private val listTopping: List<Topping>?,
    private val iClickToppingListener: IClickToppingListener
) : RecyclerView.Adapter<ToppingViewHolder>() {
    interface IClickToppingListener {
        fun onClickToppingItem(topping: Topping)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToppingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topping, parent, false)
        return ToppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToppingViewHolder, position: Int) {
        val topping = listTopping!![position]
        holder.tvName.text = topping.name
        val strPrice = "+" + topping.price + Constant.CURRENCY
        holder.tvPrice.text = strPrice
        holder.chbSelected.isChecked = topping.isSelected
        holder.chbSelected.setOnCheckedChangeListener { _: CompoundButton?, _: Boolean ->
            iClickToppingListener.onClickToppingItem(
                topping
            )
        }
    }

    override fun getItemCount(): Int {
        return listTopping?.size ?: 0
    }

    class ToppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
        val tvPrice: TextView
        val chbSelected: CheckBox

        init {
            tvName = itemView.findViewById(R.id.tv_name)
            tvPrice = itemView.findViewById(R.id.tv_price)
            chbSelected = itemView.findViewById(R.id.chb_selected)
        }
    }
}