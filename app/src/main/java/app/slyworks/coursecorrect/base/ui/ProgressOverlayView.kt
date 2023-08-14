package app.slyworks.coursecorrect.base.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import app.slyworks.coursecorrect.R


/**
 * Created by Joshua Sylvanus, 9:17 AM, 10-Dec-2022.
 */
class ProgressOverlayView
@JvmOverloads
constructor(
    context: Context,
    attrs:AttributeSet? = null,
    defStyleAttr:Int = 0): LinearLayout(context,attrs,defStyleAttr) {

        private var view: View

        init {
            view = inflate(context, R.layout.layout_progress_overlay, this)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

            view.setOnClickListener({})
        }
}