package app.slyworks.coursecorrect.courses

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.slyworks.coursecorrect.base.BaseActivity
import app.slyworks.coursecorrect.base.MOnBackPressedCallback
import app.slyworks.coursecorrect.databinding.ActivityCoursesBinding
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful


/**
 *Created by Joshua Sylvanus, 10:42 PM, 12-Apr-23.
 */
class CourseActivity : BaseActivity() {
    private lateinit var binding: ActivityCoursesBinding

    lateinit var navigator:FragmentContinuationStateful
    lateinit var viewModel: CourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initViews()
    }

    private fun initData(){
        viewModel = ViewModelProvider(this).get(CourseViewModel::class.java)

        navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this, navigator))

    }

    private fun initViews(){
        navigator.into(binding.fragmentContainer.id)
            .show(SelectCourseFragment())
            .navigate()
    }
}