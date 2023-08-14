package app.slyworks.coursecorrect.base.network

import android.annotation.SuppressLint
import android.content.Context
import com.freexitnow.data_lib.network_register.NetworkWatcherImpl
import io.reactivex.rxjava3.core.Observable


/**
 * Created by Joshua Sylvanus, 11:27 AM, 29/05/2022.
 */


@SuppressLint("NewApi")
class NetworkRegister(private val context: Context ){
    private var impl: NetworkWatcher = NetworkWatcherImpl(context)

    fun getNetworkStatus(): Boolean =
    impl.getNetworkStatus()

    fun subscribeToNetworkUpdates():Observable<Boolean> =
    impl.subscribeTo()

    fun unsubscribeToNetworkUpdates():Unit =
    impl.dispose()


}