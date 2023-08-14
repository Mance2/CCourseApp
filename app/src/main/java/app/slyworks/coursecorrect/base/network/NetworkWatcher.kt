package app.slyworks.coursecorrect.base.network

import android.app.Activity
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

/**
 *Created by Joshua Sylvanus, 5:19 PM, 1/13/2022.
 */
internal interface NetworkWatcher{
    fun getNetworkStatus():Boolean
    fun subscribeTo(): Observable<Boolean>
    fun dispose()
}