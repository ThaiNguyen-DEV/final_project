package com.app.shopfee.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.shopfee.fragment.OrderFragment
import com.app.shopfee.model.TabOrder

class OrderPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val listTabOrder: List<TabOrder>?
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return OrderFragment.newInstance(listTabOrder!![position].type)
    }

    override fun getItemCount(): Int {
        return listTabOrder?.size ?: 0
    }
}