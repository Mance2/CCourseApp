package app.slyworks.coursecorrect.base

import kotlin.UnsupportedOperationException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Created by Joshua Sylvanus, 7:06 AM, 4/2/2022.
 */

@OptIn(ExperimentalContracts::class)
class Outcome
private constructor(private val value:Any? = null) {
    val isSuccess: Boolean
    get() = value is Success<*>

    val isFailure:Boolean
    get() = value is Failure<*>

    val isError:Boolean
    get() = value is Error<*>

    fun <T> getTypedValue():T{
        when{
            isSuccess -> return (value as Success<T>).value as T
            isFailure -> return (value as Failure<T>).value as T
            isError -> return(value as Error<T>).value as T
            else -> throw UnsupportedOperationException("how far my guy?, what the f*ck are you doing?")
        }
    }

    fun getValue():Any?{
        when {
            isSuccess -> return (value as Success<*>).value
            isFailure -> return (value as Failure<*>).value
            isError -> return (value as Error<*>).value
            else -> throw UnsupportedOperationException("how far my guy?, what the f*ck are you doing?")
        }
    }

    fun getAdditionalInfo():String? {
        return when (value) {
            is Success<*> -> (value as? Success<*>)?.additionalInfo
            is Failure<*> -> (value as? Failure<*>)?.additionalInfo
            else -> (value as? Error<*>)?.additionalInfo
        }
    }

    inline fun <T> onSuccess(action:(value:T) -> Unit): Outcome {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }

        if(isSuccess)
            action(getTypedValue())
        return this
    }

    inline fun onSuccessAny(action:(value:Any) -> Unit): Outcome {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }

        if(isSuccess)
            action(getTypedValue())
        return this
    }

    inline fun <T> onFailure(action:(value:T) -> Unit): Outcome {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }

        if(isFailure)
            action(getTypedValue())
        return this
    }

    inline fun onFailureAny(action:(value:Any) -> Unit): Outcome {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }

        if(isFailure)
            action(getTypedValue())
        return this
    }

    inline fun <T> onError(action:(value:T) -> Unit): Outcome {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }

        if(isError)
            action(getTypedValue())
        return this
    }

    inline fun onErrorAny(action:(value:Any) -> Unit): Outcome {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }

        if(isError)
            action(getTypedValue())
        return this
    }


    /** @param C  type of current value held by the Outcome object.
     * @param R type to be returned in the new Outcome object
     * @param mapper function for transforming a result object of C to a result object of R
     * @throws UnsupportedOperationException probably impossible, but to make the compiler happy,
     * if Outcome being mapped is not isSuccess,isFailure or isError */
    inline fun <C,R> mapTo(mapper:(C) -> R): Outcome =
       when{
            isSuccess -> SUCCESS(mapper(getTypedValue<C>()), getAdditionalInfo())
            isFailure -> FAILURE(Unit, getAdditionalInfo())
            isError -> ERROR(Unit, getAdditionalInfo())
            else -> throw UnsupportedOperationException("should be `isSuccess`, `isFailure` or `isError`")
        }

    companion object{
        fun <T> SUCCESS(value: T? = null, additionalInfo:String? = null): Outcome =
            Outcome(Success(value, additionalInfo))
        fun <T> FAILURE(value: T? = null, reason:String? = null): Outcome =
            Outcome(Failure(value, reason))
        fun <T> ERROR(value: T? = null, additionalInfo: String? = null) : Outcome =
            Outcome(Error(value, additionalInfo))
    }

    private data class Success<T>(val value:T?, val additionalInfo:String?)
    private data class Failure<T>(val value:T?, val additionalInfo:String?)
    private data class Error<T>(val value:T?, val additionalInfo:String?)
}