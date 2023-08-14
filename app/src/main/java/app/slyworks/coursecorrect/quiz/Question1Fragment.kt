package app.slyworks.coursecorrect.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.slyworks.coursecorrect.R
import app.slyworks.coursecorrect.base.utils.NOT_ANSWERED
import app.slyworks.coursecorrect.base.utils.toggleVisibility
import app.slyworks.coursecorrect.databinding.FragmentQuestionBinding
import app.slyworks.coursecorrect.models.QuestionEntity
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful


/**
 *Created by Joshua Sylvanus, 6:34 PM, 12-Apr-23.
 */
class Question1Fragment : Fragment() {
    private lateinit var viewModel: QuizViewModel
    private lateinit var binding: FragmentQuestionBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = (requireActivity() as QuizActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        val question:QuestionEntity = viewModel.getCurrentQuestion()

        binding.tvQuestion.setText(question.question)

        if(question.options.size == 2)
            toggleVisibility(binding.rb3, binding.rb4)

        binding.rb1.setText(question.options[0])
        binding.rb2.setText(question.options[1])
        if(question.options.size > 2){
            binding.rb3.setText(question.options[2])
            binding.rb4.setText(question.options[3])
        }

        binding.rgOptions.setOnCheckedChangeListener { radioGroup, id ->
            var answer:Int = NOT_ANSWERED
            when(id){
                R.id.rb_1 -> if(binding.rb1.isChecked) answer = 0
                R.id.rb_2 -> if(binding.rb2.isChecked) answer = 1
                R.id.rb_3 -> if(binding.rb3.isChecked) answer = 2
                R.id.rb_4 -> if(binding.rb4.isChecked) answer = 3
                else -> throw UnsupportedOperationException()
            }

            if(answer != NOT_ANSWERED)
               viewModel.setAnswerForCurrentQuestion(answer)
        }
    }
}