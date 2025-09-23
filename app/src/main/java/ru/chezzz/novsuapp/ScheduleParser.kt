package ru.chezzz.novsuapp


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object ScheduleParser {
    suspend fun loadSchedule(url: String): List<Lesson> = withContext(Dispatchers.IO) {
        val doc = Jsoup.connect(url).get()
        val lessons = mutableListOf<Lesson>()

        var currentDay: String? = null

        // Ищем строки таблицы shedultable
        val rows = doc.select("table.shedultable tbody tr")
        for (row in rows) {
            val cells = row.select("td")

            if (cells.isEmpty()) continue

            // Если ячейка содержит день недели (например "Пн", "Вт"...)
            // обычно она идёт первой и имеет rowspan > 1
            if (cells.size == 1 && cells[0].select("b").isNotEmpty()) {
                currentDay = cells[0].text().trim()
                continue
            }

            // строки с уроками
            if (cells.size >= 5) {
                var startTime = ""
                var endTime = ""

                // timeCell = "14:00 15:00" пример
                val timeCell = cells[0].text().split(' ').filter { it.isNotBlank() }

                try {
                    startTime = timeCell[0]  // теперь изменяем наружный var
                } catch (e: Exception) {
                    startTime = ""
                }

                try {
                    val endTimeArr = timeCell.last().split(":").toMutableList()
                    if (endTimeArr.isNotEmpty()) {
                        endTimeArr[0] = endTimeArr[0].toString()
                        endTimeArr[1] = "45"
                        endTime = endTimeArr.joinToString(":") // изменяем наружный var
                    }
                } catch (e: Exception) {
                    endTime = ""
                }

                lessons.add(
                    Lesson(
                        dayOfWeek = currentDay ?: "",
                        startTime = startTime,
                        endTime = endTime,
                        type = cells[2].text().trim(),
                        subject = cells[2].text().trim(),
                        teacher = cells.getOrNull(3)?.text(),
                        room = cells.getOrNull(4)?.text(),
                        comment = cells.getOrNull(5)?.text()
                    )
                )

            }
        }
        lessons
    }

    private fun parseTimeRange(raw: String?): Pair<String, String> {
        if (raw.isNullOrBlank()) return "" to ""
        val times = raw.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        return if (times.size >= 2) times[0] to times[1] else times[0] to ""
    }
}