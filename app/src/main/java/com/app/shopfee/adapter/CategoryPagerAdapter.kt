package com.app.shopfee.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.shopfee.fragment.DrinkFragment
import com.app.shopfee.model.Category

class CategoryPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val listCategory: List<Category>?
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return DrinkFragment.newInstance(listCategory!![position].id)
    }

    override fun getItemCount(): Int {
        return listCategory?.size ?: 0
    }
}