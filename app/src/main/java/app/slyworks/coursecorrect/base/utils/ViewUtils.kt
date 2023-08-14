package app.slyworks.coursecorrect.base.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import app.slyworks.coursecorrect.App
import app.slyworks.coursecorrect.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import app.slyworks.coursecorrect.base.ui.MessageDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


/**
 * Created by Joshua Sylvanus, 8:10 AM, 26/04/2022.
 */

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val EditText.properText:String
    get() = this.text.toString().trim()

val TextView.properText:String
    get() = this.text.toString().trim()

/**
 * show a Snackbar with duration set to #Snackbar.LENGTH_LONG */
fun Context.showToast(message:String):Unit =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun showToast(message:String):Unit =
    Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show()


fun ImageView.displayImage(imageID: Int) {
    Glide.with(this.context)
        .load(imageID)
        .dontTransform()
        .into(this);
}

fun ImageView.displayImage(imageID: String) {
    Glide.with(this.context)
        .load(imageID)
        .dontTransform()
        .into(this);
}

fun ImageView.displayImage(imageID: Uri) {
    Glide.with(this.context)
        .load(imageID)
        .dontTransform()
        .into(this);
}

fun ImageView.displayImage(image: Bitmap) {
    Glide.with(this.context)
        .load(image)
        .dontTransform()
        .into(this);
}

fun ImageView.displayGif(imageID: Int) {
    Glide.with(this.context)
        .asGif()
        .load(imageID)
        .dontTransform()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this);
}

fun ImageView.displayGif(imageID: String) {
    Glide.with(this.context)
        .asGif()
        .load(imageID)
        .dontTransform()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this);
}

fun Activity.setStatusBarVisibility(status: Boolean){
    /* remember this is cleared when user navigates away from the activity */
   if(status)
       window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
   else {
       window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
       findViewById<ViewGroup>(android.R.id.content).getChildAt(0).setFitsSystemWindows(true) /*.getchildAt(0)*/
   }
}

fun Activity.setStatusBarColor(@ColorRes colorResId: Int = R.color.app_pink){
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.setStatusBarColor(ContextCompat.getColor(this, colorResId))
}

fun Activity.closeKeyboard(){
    val inputManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus?.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
}

fun displayMessage(message:String, view:View):Unit =
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

fun displayErrorDialog(message:String,
                       fm:FragmentManager,
                       prompt:String = "ok",
                       isCancellable:Boolean = false,
                       onClick:((MessageDialog) -> Unit)? = null):Unit =
    MessageDialog(message, prompt, isCancellable, onClick)
        .show(fm, "")

fun Activity.makeFullScreen(){
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS )
}

fun EditText.setOnEditorAction(func:() -> Unit){
    setOnEditorActionListener { textView, i, keyEvent ->
        if(id == textView.id) {
            func()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}

fun EditText.setOnEditorAction(@IdRes id:Int){
    setOnEditorActionListener { textView, _id:Int?, _:KeyEvent ->
            (context as AppCompatActivity).closeKeyboard()
            textView.rootView.findViewById<View>(id).requestFocus()
            return@setOnEditorActionListener true

        return@setOnEditorActionListener false
    }
}

fun View.toggleVisibility(status:Boolean? = null){
    if(status != null){
        if(status) visibility = View.VISIBLE
        else visibility = View.GONE

        return
    }

    when(visibility){
        View.VISIBLE -> visibility = View.GONE
        View.GONE -> visibility = View.VISIBLE
        View.INVISIBLE -> visibility = View.VISIBLE
    }
}

fun toggleVisibility(vararg views:View) =
    views.toList().forEach { it.toggleVisibility() }

fun toggleVisibility(status: Boolean, vararg views:View) =
    views.toList().forEach { it.toggleVisibility(status) }