package app.slyworks.coursecorrect.auth

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.slyworks.coursecorrect.base.BaseActivity
import app.slyworks.coursecorrect.base.BaseViewModel
import app.slyworks.coursecorrect.base.MOnBackPressedCallback
import app.slyworks.coursecorrect.base.utils.displayErrorDialog
import app.slyworks.coursecorrect.base.utils.toggleVisibility
import app.slyworks.coursecorrect.databinding.ActivityAuthBinding
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful


/**
 *Created by Joshua Sylvanus, 11:58 PM, 12-Apr-23.
 */
class AuthActivity : BaseActivity() {
   lateinit var navigator:FragmentContinuationStateful
   lateinit var viewModel:AuthViewModel

   private lateinit var binding:ActivityAuthBinding

    override fun cancelOngoingOperation() {
        displayErrorDialog("an operation is in progress, are you sure you you want to cancel?", supportFragmentManager)
        {
            it.dismiss()
            (viewModel as BaseViewModel).cancelOngoingOperations()
            binding.progressOverlay.toggleVisibility(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initViews()
    }

    private fun initData(){
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this, navigator))

        viewModel.uiStateLD.observe(this){
            when(it){
                is AuthUIState.LoadingStarted -> binding.progressOverlay.toggleVisibility(true)
                is AuthUIState.LoadingStopped -> binding.progressOverlay.toggleVisibility(false)
                else -> {}
            }
        }
    }

    private fun initViews(){
        navigator.into(binding.fragmentContainer.id)
            .show(LoginFragment())
            .navigate()
    }
}