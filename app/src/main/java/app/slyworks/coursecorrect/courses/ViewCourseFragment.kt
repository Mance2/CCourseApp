package app.slyworks.coursecorrect.courses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.slyworks.coursecorrect.base.utils.plusAssign
import app.slyworks.coursecorrect.base.utils.toggleVisibility
import app.slyworks.coursecorrect.databinding.FragmentSelectCourseBinding
import app.slyworks.coursecorrect.databinding.FragmentViewCourseBinding
import app.slyworks.coursecorrect.models.CourseEntity
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import io.reactivex.rxjava3.disposables.CompositeDisposable


/**
 *Created by Joshua Sylvanus, 6:34 PM, 12-Apr-23.
 */
class ViewCourseFragment(private val course: CourseEntity) : Fragment() {
    private lateinit var navigator: FragmentContinuationStateful
    private lateinit var viewModel: CourseViewModel
    private lateinit var binding: FragmentViewCourseBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = (requireActivity() as CourseActivity).navigator
        viewModel = (requireActivity() as CourseActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentViewCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
       binding.tvCourse.setText(course.content)

       binding.ivArrowBack.setOnClickListener {
           requireActivity().onBackPressedDispatcher.onBackPressed()
       }
    }
}