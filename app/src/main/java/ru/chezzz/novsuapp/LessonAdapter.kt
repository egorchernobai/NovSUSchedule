package ru.chezzz.novsuapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LessonsAdapter(private val lessons: List<Lesson>) :
    RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.subject)
        val type: TextView = itemView.findViewById(R.id.type)
        val time: TextView = itemView.findViewById(R.id.time)
        val teacher: TextView = itemView.findViewById(R.id.teacher)
        val room: TextView = itemView.findViewById(R.id.room)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.subject.text = lesson.subject

        holder.time.text = lesson.startTime + " - " + lesson.endTime;
        holder.time.visibility = if (lesson.startTime.isNullOrBlank()) View.GONE else View.VISIBLE

        holder.type.text = lesson.comment
        holder.type.visibility = if (lesson.comment.isNullOrBlank()) View.GONE else View.VISIBLE

        holder.teacher.text = lesson.teacher ?: ""
        holder.teacher.visibility = if (lesson.teacher.isNullOrBlank()) View.GONE else View.VISIBLE

        holder.room.text = lesson.room ?: ""
        holder.room.visibility = if (lesson.room.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    override fun getItemCount(): Int = lessons.size
}
