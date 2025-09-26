package ru.chezzz.novsuapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        lifecycleScope.launch {
            val lessons = ScheduleParser.loadSchedule(
                "https://portal.novsu.ru/univer/timetable/ochn/i.1103357/?page=EditViewGroup&instId=2159922&name=3093&type=%D0%94%D0%9E&year=2023"
            )
            val daysMap = lessons.groupBy { it.dayOfWeek }

            val adapter = DaysPagerAdapter(this@MainActivity, daysMap)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = adapter.getDayTitle(position)
            }.attach()

            // --- Открываем сегодняшнюю вкладку ---
            val today = LocalDate.now().dayOfWeek
            val todayName = when (today) {
                DayOfWeek.MONDAY -> "Пн"
                DayOfWeek.TUESDAY -> "Вт"
                DayOfWeek.WEDNESDAY -> "Ср"
                DayOfWeek.THURSDAY -> "Чт"
                DayOfWeek.FRIDAY -> "Пт"
                DayOfWeek.SATURDAY -> "Сб"
                DayOfWeek.SUNDAY -> "Пн"
            }

            val todayIndex = adapter.getDayTitles().indexOf(todayName)
            if (todayIndex != -1) {
                viewPager.setCurrentItem(todayIndex, false)
            }
        }
    }
}
