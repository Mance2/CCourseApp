package app.slyworks.coursecorrect.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.slyworks.coursecorrect.R
import app.slyworks.coursecorrect.databinding.LiCourseBinding
import app.slyworks.coursecorrect.models.CourseEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


/**
 *Created by Joshua Sylvanus, 11:42 PM, 12-Apr-23.
 */
class RVCoursesAdapter : RecyclerView.Adapter<RVCoursesAdapter.ViewHolder>(){
    private var list:List<CourseEntity> = emptyList()
    private val clickedItemSubject:PublishSubject<CourseEntity> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVCoursesAdapter.ViewHolder {
        val binding:LiCourseBinding = LiCourseBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RVCoursesAdapter.ViewHolder, position: Int):Unit =
       holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    fun setDataList(list:List<CourseEntity>){
        this.list = list
        notifyDataSetChanged()
    }

    fun getClickedItemSubject(): Observable<CourseEntity> = clickedItemSubject.hide()

    inner class ViewHolder(val binding:LiCourseBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(entity:CourseEntity){
            binding.tvTitle.setText(entity.title)
            binding.tvDetails.setText(
                binding.root.context.getString(
                    R.string.estimated_reading_time_placeholder,
                    entity.details ) )

            binding.root.setOnClickListener {
                clickedItemSubject.onNext(entity)
            }
        }
    }
}