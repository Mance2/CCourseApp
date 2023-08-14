package app.slyworks.coursecorrect.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

/**
 * Created by Joshua Sylvanus, 6:26 PM, 13/1/2022.
 */
open class BaseViewModel : ViewModel(){
    open fun cancelOngoingOperations(){
        viewModelScope.coroutineContext[Job]?.cancelChildren()
    }
}