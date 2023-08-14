package app.slyworks.coursecorrect.quiz

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import app.slyworks.coursecorrect.R
import app.slyworks.coursecorrect.base.ActivityHelper
import app.slyworks.coursecorrect.base.BaseActivity
import app.slyworks.coursecorrect.base.MOnBackPressedCallback
import app.slyworks.coursecorrect.base.ui.ExitDialog
import app.slyworks.coursecorrect.base.ui.ProgressOverlayView
import app.slyworks.coursecorrect.base.utils.displayErrorDialog
import app.slyworks.coursecorrect.base.utils.toggleVisibility
import app.slyworks.coursecorrect.databinding.ActivityQuizBinding
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful

class QuizActivity : BaseActivity() {
    lateinit var navigator:FragmentContinuationStateful
    lateinit var viewModel: QuizViewModel

    private var hasQuizResultFragmentBeenShown:Boolean = false

    private val supplier1:() -> Fragment = ::Question1Fragment
    private val supplier2:() -> Fragment = ::Question2Fragment
    private var currentSupplier:() -> Fragment = supplier1

    private lateinit var binding: ActivityQuizBinding

    override fun onStop() {
        super.onStop()
        viewModel.resetUIState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initViews()
    }

    private fun initData(){
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

        this.onBackPressedDispatcher
            .addCallback(this, object :OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val view: ProgressOverlayView? = findViewById(R.id.progress_overlay)
                    if(view != null && view.isVisible){
                        cancelOngoingOperation()
                        return
                    }

                    if(hasQuizResultFragmentBeenShown) {
                        finish()
                       return
                    }

                    displayErrorDialog(
                        "Are you sure you want to leave the Quiz?",
                        supportFragmentManager,
                        "leave quiz")
                    {
                      it.dismiss()
                      finish()
                    }
                }
            })

        viewModel.countDownLD.observe(this){
            binding.tvCountdown.setText(it)
        }

        viewModel.uiStateLD.observe(this){
            when(it){
                is QuizUIState.QuizFinished ->{
                    binding.lPrimary.toggleVisibility(false)
                    binding.fragmentContainerSecondary.toggleVisibility(true)

                    val t:FragmentTransaction = supportFragmentManager.beginTransaction()
                    t.setCustomAnimations(
                        dev.joshuasylvanus.navigator.R.anim.enter,
                        dev.joshuasylvanus.navigator.R.anim.exit,
                        dev.joshuasylvanus.navigator.R.anim.pop_enter,
                        dev.joshuasylvanus.navigator.R.anim.pop_exit )
                    t.replace(binding.fragmentContainerSecondary.id, QuizResultFragment())
                    t.commit()
                    hasQuizResultFragmentBeenShown = true
                }

                is QuizUIState.NextQuestion -> {
                    binding.progressQuestion.setProgress(it.currentProgress)
                    binding.tvQuestionNumber.setText(it.currentQuestionString)

                    if(currentSupplier == supplier1)
                        currentSupplier = supplier2
                    else
                        currentSupplier = supplier1

                    navigator.replace(currentSupplier())
                        .navigate()
                }
                is QuizUIState.CountDownFinished -> viewModel.initQuiz()
                else -> {}
            }
        }
    }

    private fun initViews(){
        navigator.into(binding.fragmentContainer.id)
            .show(Question1Fragment())
            .navigate()

        binding.progressQuestion.setProgress(viewModel.getCurrentProgress())


        binding.ivArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        binding.btnNext.setOnClickListener {
            if(!viewModel.hasCurrentQuestionBeenAnswered()){
                displayErrorDialog("please select an option to proceed to next question", supportFragmentManager)
                return@setOnClickListener
            }

            viewModel.initQuiz()
        }

        viewModel.initQuiz()
    }
}