package com.app.shopfee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.R
import com.app.shopfee.adapter.FilterAdapter.FilterViewHolder
import com.app.shopfee.model.Filter

class FilterAdapter(
    private var context: Context?,
    private val listFilter: List<Filter>?,
    private val iClickFilterListener: IClickFilterListener
) : RecyclerView.Adapter<FilterViewHolder>() {

    interface IClickFilterListener {
        fun onClickFilterItem(filter: Filter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter = listFilter!![position]
        when (filter.id) {
            Filter.TYPE_FILTER_ALL -> holder.imgFilter.setImageResource(R.drawable.ic_filter)
            Filter.TYPE_FILTER_RATE -> holder.imgFilter.setImageResource(R.drawable.ic_rate)
            Filter.TYPE_FILTER_PRICE -> holder.imgFilter.setImageResource(R.drawable.ic_price)
            Filter.TYPE_FILTER_PROMOTION -> holder.imgFilter.setImageResource(R.drawable.ic_promotion)
        }
        holder.tvTitle.text = filter.name
        if (filter.isSelected) {
            holder.layoutItem.setBackgroundResource(R.drawable.bg_button_enable_corner_16)
            val color = ContextCompat.getColor(context!!, R.color.white)
            holder.tvTitle.setTextColor(color)
            holder.imgFilter.setColorFilter(color)
        } else {
            holder.layoutItem.setBackgroundResource(R.drawable.bg_button_disable_corner_16)
            val color = ContextCompat.getColor(context!!, R.color.black)
            holder.tvTitle.setTextColor(color)
            holder.imgFilter.setColorFilter(color)
        }
        holder.layoutItem.setOnClickListener {
            iClickFilterListener.onClickFilterItem(
                filter
            )
        }
    }

    override fun getItemCount(): Int {
        return listFilter?.size ?: 0
    }

    fun release() {
        if (context != null) context = null
    }

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFilter: ImageView
        val tvTitle: TextView
        val layoutItem: LinearLayout

        init {
            imgFilter = itemView.findViewById(R.id.img_filter)
            tvTitle = itemView.findViewById(R.id.tv_title)
            layoutItem = itemView.findViewById(R.id.layout_item)
        }
    }
}