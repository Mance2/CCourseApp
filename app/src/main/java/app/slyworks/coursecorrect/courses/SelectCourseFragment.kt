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
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import io.reactivex.rxjava3.disposables.CompositeDisposable


/**
 *Created by Joshua Sylvanus, 6:34 PM, 12-Apr-23.
 */
class SelectCourseFragment : Fragment() {
    private val disposables:CompositeDisposable = CompositeDisposable()

    private lateinit var adapter:RVCoursesAdapter
    private lateinit var navigator: FragmentContinuationStateful
    private lateinit var viewModel: CourseViewModel
    private lateinit var binding: FragmentSelectCourseBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = (requireActivity() as CourseActivity).navigator
        viewModel = (requireActivity() as CourseActivity).viewModel
    }

    override fun onStop() {
        super.onStop()
        viewModel.resetUIState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSelectCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initData()
    }

    private fun initData(){
        disposables +=
        adapter.getClickedItemSubject()
            .subscribe{
                navigator.show(ViewCourseFragment(it))
                    .navigate()
            }

        viewModel.uiStateLD.observe(viewLifecycleOwner){
            when(it){
                is CourseUIState.LoadingStopped -> {
                    binding.shimmerCourses.stopShimmer()
                    binding.shimmerCourses.toggleVisibility(false)
                }
                is CourseUIState.CourseList -> {
                    binding.shimmerCourses.stopShimmer()
                    binding.shimmerCourses.toggleVisibility(false)
                    binding.rvCourses.toggleVisibility(true)
                    adapter.setDataList(it.list)
                }
                else ->{}
            }
        }

        viewModel.getCourses()
    }

    private fun initViews(){
        adapter = RVCoursesAdapter()
        binding.rvCourses.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.rvCourses.adapter = adapter

        binding.ivArrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}