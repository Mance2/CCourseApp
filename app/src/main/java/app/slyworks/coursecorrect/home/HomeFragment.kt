package app.slyworks.coursecorrect.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.slyworks.coursecorrect.courses.CourseActivity
import app.slyworks.coursecorrect.databinding.FragmentHomeBinding
import app.slyworks.coursecorrect.quiz.QuizActivity
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful


/**
 *Created by Joshua Sylvanus, 6:34 PM, 12-Apr-23.
 */
class HomeFragment : Fragment() {
    private lateinit var navigator: FragmentContinuationStateful
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigator = (requireActivity() as HomeActivity).navigator
        viewModel = (requireActivity() as HomeActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        binding.lAppBar.tvWelcome.setText(viewModel.getTimeOfDay())
        binding.lAppBar.tvUsername.setText(viewModel.getUsername())

        binding.lAppBar.ivToggle.setOnClickListener {
            (requireActivity() as HomeActivity).toggleDrawerState()
        }

        binding.cvStudy.setOnClickListener{
            Navigator.intentFor<CourseActivity>(requireActivity())
                .navigate()
        }

        binding.cvQuiz.setOnClickListener{
            Navigator.intentFor<QuizActivity>(requireActivity())
                .navigate()
        }
    }
}