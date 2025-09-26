package ru.chezzz.novsuapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        lifecycleScope.launch {
            val lessons = ScheduleParser.loadSchedule("https://portal.novsu.ru/univer/timetable/ochn/i.1103357/?page=EditViewGroup&instId=2159922&name=3093&type=%D0%94%D0%9E&year=2023")
            val daysMap = lessons.groupBy { it.dayOfWeek }

            val adapter = DaysPagerAdapter(this@MainActivity, daysMap)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = adapter.getDayTitle(position)
            }.attach()

            // --- Select tab by day ---
            val calendar = Calendar.getInstance()
            val index = calendar.get(Calendar.DAY_OF_WEEK) - 2 // 1 = Sunday ... 7 = Saturday
            if (index >= 0) {
                viewPager.setCurrentItem(index, false)
            }
        }
    }
}
