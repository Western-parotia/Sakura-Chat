package com.nmm.chart.v2.base.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * create by zhusw on 7/12/21 18:05
 */
class ViewPager2FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    constructor(frag: Fragment) : this(frag.childFragmentManager, frag.lifecycle)
    constructor(act: AppCompatActivity) : this(act.supportFragmentManager, act.lifecycle)

    /**
     * 由于notify默认不会换掉fragment，所以在每次修改list时记录一个唯一值，然后重写[getItemId]改掉id，让fragment重新创建
     */
    private var listNotifyMillis = 0L

    var list = listOf<Fragment>()
        set(value) {
            field = value
            listNotifyMillis = System.currentTimeMillis()
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]

    override fun getItemId(position: Int): Long {
        return listNotifyMillis + super.getItemId(position)
    }
}