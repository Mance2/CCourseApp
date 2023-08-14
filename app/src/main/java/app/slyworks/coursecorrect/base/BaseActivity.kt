package app.slyworks.coursecorrect.base

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.slyworks.coursecorrect.base.utils.KEY_ACTIVITY_COUNT

open class BaseActivity : AppCompatActivity() {

    open fun cancelOngoingOperation(){}

    override fun onDestroy() {
        super.onDestroy()
        ActivityHelper.decrementActivityCount()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null)
            ActivityHelper.setActivityCount(savedInstanceState.getInt(KEY_ACTIVITY_COUNT) - 1)

        ActivityHelper.incrementActivityCount()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_ACTIVITY_COUNT, ActivityHelper.getActivityCount())
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed(){
       onBackPressedFix()
    }

    private fun onBackPressedFix(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.Q &&
            isTaskRoot &&
            (supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.backStackEntryCount ?: 0) == 0 &&
            supportFragmentManager.backStackEntryCount == 0)
            finishAfterTransition()
        else
            super.onBackPressed()
    }

}