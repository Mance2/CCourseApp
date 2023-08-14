package app.slyworks.coursecorrect.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.slyworks.coursecorrect.App
import app.slyworks.coursecorrect.Repository
import app.slyworks.coursecorrect.base.BaseViewModel
import app.slyworks.coursecorrect.base.Outcome
import app.slyworks.coursecorrect.base.network.NetworkRegister
import app.slyworks.coursecorrect.base.utils.NETWORK_ERROR_MESSAGE
import app.slyworks.coursecorrect.base.utils.displayErrorDialog
import app.slyworks.coursecorrect.models.UserDetails
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 12:27 AM, 13-Apr-23.
 */
sealed class AuthUIState{
    object Default : AuthUIState()
    object LoadingStarted : AuthUIState()
    object LoadingStopped : AuthUIState()
    object LoginSuccess : AuthUIState()
    object SignupSuccess : AuthUIState()
    data class Message(val message:String) : AuthUIState()
}

class AuthViewModel : BaseViewModel() {
   private val _uiStateLD: MutableLiveData<AuthUIState> = MutableLiveData(AuthUIState.Default)
   val uiStateLD: LiveData<AuthUIState> = _uiStateLD

    private val networkRegister:NetworkRegister = NetworkRegister(App.getContext())
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun resetUIState(){ _uiStateLD.value = AuthUIState.Default }
   fun login(email:String, password:String){
       if(!networkRegister.getNetworkStatus()){
           _uiStateLD.setValue(AuthUIState.Message(NETWORK_ERROR_MESSAGE))
           return
       }

       _uiStateLD.setValue(AuthUIState.LoadingStarted)

       autheticate(email,password)
           .subscribeOn(Schedulers.io())
           .observeOn(Schedulers.io())
           .subscribe({
                _uiStateLD.postValue(AuthUIState.LoadingStopped)

                if(!it.isSuccess)
                    _uiStateLD.postValue(AuthUIState.Message(it.getAdditionalInfo()!!))
                else {
                    Repository.setUsername(it.getTypedValue<UserDetails>().first_name)
                    _uiStateLD.postValue(AuthUIState.LoginSuccess)
                }
           },{
               Timber.e(it.message)
               _uiStateLD.postValue(AuthUIState.LoadingStopped)
               _uiStateLD.postValue(AuthUIState.Message("an error occurred. Please try again"))
           })
   }

    fun signup(firstName: String, lastName: String,email:String, password:String){
       if(!networkRegister.getNetworkStatus()){
           _uiStateLD.setValue(AuthUIState.Message(NETWORK_ERROR_MESSAGE))
           return
       }

       _uiStateLD.setValue(AuthUIState.LoadingStarted)

       register(firstName, lastName, email, password)
           .subscribeOn(Schedulers.io())
           .observeOn(Schedulers.io())
           .subscribe({
                _uiStateLD.postValue(AuthUIState.LoadingStopped)

                if(!it.isSuccess)
                    _uiStateLD.postValue(AuthUIState.Message(it.getAdditionalInfo()!!))
                else {
                    Repository.setUsername(it.getTypedValue<UserDetails>().first_name)
                    _uiStateLD.postValue(AuthUIState.SignupSuccess)
                }
           },{
               Timber.e(it.message)
               _uiStateLD.postValue(AuthUIState.LoadingStopped)
               _uiStateLD.postValue(AuthUIState.Message("an error occurred. Please try again"))
           })
   }


   private fun autheticate(email: String, password: String):Single<Outcome> =
       __login(email,password)
           .flatMap {
               if(!it.isSuccess)
                   return@flatMap Single.just(it)
               else
                   return@flatMap __getDetails(email)
           }

    private fun __login(email: String, password: String):Single<Outcome> =
        Single.create { emitter ->
          firebaseAuth.signInWithEmailAndPassword(email, password)
              .addOnCompleteListener {
                  val r:Outcome
                  if(it.isSuccessful)
                      r = Outcome.SUCCESS(Unit)
                  else
                      r = Outcome.FAILURE(Unit, it.exception?.message)

                  emitter.onSuccess(r)
              }
        }

   private fun __getDetails(email:String):Single<Outcome> =
       Single.create { emitter ->
           firebaseDatabase.reference
               .child("users")
               .child(email.substring(0,6))
               .get()
               .addOnCompleteListener {
                   val r:Outcome
                   if(it.isSuccessful) {
                       val userDetails: UserDetails = it.result!!.getValue(UserDetails::class.java)!!
                       r = Outcome.SUCCESS(userDetails)
                   }else
                       r = Outcome.FAILURE(Unit, it.exception?.message)

                   emitter.onSuccess(r)
               }
       }



    private fun register(firstName: String, lastName: String,email: String, password: String):Single<Outcome> =
        __signup(email,password)
            .flatMap {
                if(!it.isSuccess)
                    return@flatMap Single.just(it)
                else
                    return@flatMap __saveDetails(email,firstName,lastName)
            }

   private fun __signup(email: String, password: String):Single<Outcome> =
       Single.create { emitter ->
           firebaseAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener{
                   val r:Outcome
                   if(it.isSuccessful)
                       r = Outcome.SUCCESS(Unit)
                   else
                       r = Outcome.FAILURE(Unit, it.exception?.message)

                   emitter.onSuccess(r)
               }
       }

   private fun __saveDetails(email:String,firstName:String, lastName:String):Single<Outcome> =
       Single.create { emitter ->
           val details:UserDetails = UserDetails(firstName,lastName)
           firebaseDatabase.reference
               .child("users")
               .child(email.substring(0,6))
               .setValue(details)
               .addOnCompleteListener {
                   val r:Outcome

                   if(it.isSuccessful)
                       r = Outcome.SUCCESS(UserDetails(firstName,lastName))
                   else
                       r = Outcome.FAILURE(Unit, it.exception?.message)

                   emitter.onSuccess(r)
               }
       }
}