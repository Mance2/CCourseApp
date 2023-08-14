package app.slyworks.coursecorrect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.slyworks.coursecorrect.auth.AuthActivity
import app.slyworks.coursecorrect.base.BaseActivity
import app.slyworks.coursecorrect.base.utils.makeFullScreen
import com.google.firebase.FirebaseApp
import dev.joshuasylvanus.navigator.Navigator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 *Created by Joshua Sylvanus, 1:08 AM, 13-Apr-23.
 */
class SplashActivity : AppCompatActivity() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        makeFullScreen()

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        disposables.clear()
        val d: Disposable =
            Observable.timer(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(::initViews, ::initViewsError)
        disposables.add(d)
    }

    private fun initViews(l:Long){
        Navigator.intentFor<AuthActivity>(this)
            .newAndClearTask()
            .navigate()
    }

    private fun initViewsError(t:Throwable){
        Timber.e("error occurred: ${t.message}")

        Navigator.intentFor<AuthActivity>(this)
            .newAndClearTask()
            .navigate()
    }
}