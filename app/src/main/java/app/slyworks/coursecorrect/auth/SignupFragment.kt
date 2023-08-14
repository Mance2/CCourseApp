package app.slyworks.coursecorrect.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.slyworks.coursecorrect.R
import app.slyworks.coursecorrect.base.utils.closeKeyboard
import app.slyworks.coursecorrect.base.utils.displayErrorDialog
import app.slyworks.coursecorrect.base.utils.properText
import app.slyworks.coursecorrect.base.utils.toggleVisibility
import app.slyworks.coursecorrect.databinding.FragmentLoginBinding
import app.slyworks.coursecorrect.databinding.FragmentQuestionBinding
import app.slyworks.coursecorrect.databinding.FragmentSignupBinding
import app.slyworks.coursecorrect.home.HomeActivity
import app.slyworks.coursecorrect.models.QuestionEntity
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful


/**
 *Created by Joshua Sylvanus, 6:34 PM, 12-Apr-23.
 */
class SignupFragment : Fragment() {
    private lateinit var navigator: FragmentContinuationStateful
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentSignupBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigator = (requireActivity() as AuthActivity).navigator
        viewModel = (requireActivity() as AuthActivity).viewModel
    }

    override fun onStop() {
        super.onStop()
        viewModel.resetUIState()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initViews()
    }

    private fun initData(){
        viewModel.uiStateLD.observe(viewLifecycleOwner){
            when(it){
                is AuthUIState.SignupSuccess ->
                    Navigator.intentFor<HomeActivity>(requireActivity())
                        .newAndClearTask()
                        .navigate()
                is AuthUIState.Message ->
                    displayErrorDialog(it.message, childFragmentManager)
                else -> {}
            }
        }
    }
    private fun initViews(){
        binding.ivArrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSignup.setOnClickListener {
            requireActivity().closeKeyboard()

            if(!check())
                return@setOnClickListener

            val firstName:String = binding.etFirstName.properText
            val lastName:String = binding.etLastName.properText
            val email:String = binding.etEmail.properText
            val password:String = binding.etPassword.properText
            viewModel.signup(firstName, lastName,email, password)
        }
    }

    private fun check():Boolean {
        if(binding.etFirstName.properText.isEmpty()){
            displayErrorDialog("please enter your first name", childFragmentManager)
            return false
        }

        if(binding.etLastName.properText.isEmpty()){
            displayErrorDialog("please enter your last name", childFragmentManager)
            return false
        }

        if(binding.etEmail.properText.isEmpty()){
            displayErrorDialog("please enter your email", childFragmentManager)
            return false
        }
        if(!binding.etEmail.properText.contains("@") || !binding.etEmail.properText.contains(".")){
            displayErrorDialog("please enter a valid email", childFragmentManager)
            return false
        }

        if(binding.etPassword.properText.isEmpty()){
            displayErrorDialog("please enter your password", childFragmentManager)
            return false
        }

        if(binding.etPassword.properText.length < 8){
            displayErrorDialog("please enter a password of at least 8 characters", childFragmentManager)
            return false
        }

        return true
    }
}