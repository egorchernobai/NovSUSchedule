package ru.chezzz.novsuapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DaysPagerAdapter(
    fa: FragmentActivity,
    private val daysMap: Map<String, List<Lesson>>
) : FragmentStateAdapter(fa) {

    private val days = daysMap.keys.toList()

    override fun getItemCount(): Int = days.size

    override fun createFragment(position: Int): Fragment {
        val day = days[position]
        return DayFragment(daysMap[day] ?: emptyList())
    }

    fun getDayTitle(position: Int): String = days[position]
}
