package app.slyworks.coursecorrect.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.slyworks.coursecorrect.R
import app.slyworks.coursecorrect.databinding.LiCourseBinding
import app.slyworks.coursecorrect.databinding.LiQuizResultBinding
import app.slyworks.coursecorrect.models.CourseEntity
import app.slyworks.coursecorrect.models.QuestionEntity
import app.slyworks.coursecorrect.models.QuizResultEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


/**
 * Created by Joshua Sylvanus, 11:42 PM, 12-Apr-23.
 */
class RVQuizResultsAdapter : RecyclerView.Adapter<RVQuizResultsAdapter.ViewHolder>(){
    private var list:List<QuestionEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVQuizResultsAdapter.ViewHolder {
        val binding:LiQuizResultBinding = LiQuizResultBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RVQuizResultsAdapter.ViewHolder, position: Int):Unit =
       holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    fun setDataList(list:List<QuestionEntity>){
        this.list = list
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding:LiQuizResultBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(entity:QuestionEntity){
            binding.tvQuestion.setText("(${list.indexOf(entity)+1}) ${entity.question}")
            binding.tvAnswer.setText(entity.options[entity.answerIndex])

        }
    }
}