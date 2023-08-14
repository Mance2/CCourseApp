package app.slyworks.coursecorrect

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import timber.log.Timber


/**
 *Created by Treasure Onu, 6:31 PM, 12-Apr-23.
 */
class App : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var context:Context

        @JvmStatic
        private fun setContext(ctx:Context){
            context = ctx
        }

        @JvmStatic
        fun getContext():Context = context
    }
    override fun onCreate() {
        super.onCreate()

        initContext()
        initTimber()
    }

    private fun initContext(){
        setContext(this)
    }

    private fun initFirebase(){
        FirebaseApp.initializeApp(this)
    }

    private fun initTimber(){
        if(!BuildConfig.DEBUG)
            return

        Timber.plant(object: Timber.DebugTree(){
            override fun createStackElementTag(element: StackTraceElement): String {
                return String.format(
                    "%s:%s",
                    element.methodName,
                    super.createStackElementTag(element))
            }
        })
    }
}