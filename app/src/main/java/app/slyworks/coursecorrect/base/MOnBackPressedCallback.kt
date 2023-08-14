package app.slyworks.coursecorrect.base

import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import app.slyworks.coursecorrect.R
import app.slyworks.coursecorrect.base.ui.ExitDialog
import app.slyworks.coursecorrect.base.ui.ProgressOverlayView
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful

open class MOnBackPressedCallback(private val activity: BaseActivity,
                                  private val navigator:FragmentContinuationStateful? = null)
    : OnBackPressedCallback(true) {

    private val func:() -> Unit = if(navigator != null) ::newImpl else ::oldImpl

    override fun handleOnBackPressed() { func() }

    private fun newImpl(){
        val view: ProgressOverlayView? = activity.findViewById(R.id.progress_overlay)
        if(view != null && view.isVisible){
            activity.cancelOngoingOperation()
            return
        }

        if(!navigator!!.popBackStack()){
            if(!ActivityHelper.isLastActivity())
                activity.finish()
            else
                ExitDialog().show(activity.supportFragmentManager, "")
        }
    }

    private fun oldImpl(){
        val view: ProgressOverlayView? = activity.findViewById(R.id.progress_overlay)
        if(view != null && view.isVisible){
            activity.cancelOngoingOperation()
            return
        }

        if(!ActivityHelper.isLastActivity()){
            isEnabled = false
            activity.onBackPressed()
            return
        }

        ExitDialog().show(activity.supportFragmentManager, "exit dialog")
    }
}