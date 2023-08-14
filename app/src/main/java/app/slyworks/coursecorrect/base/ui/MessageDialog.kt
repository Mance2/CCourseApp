package app.slyworks.coursecorrect.base.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.slyworks.coursecorrect.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * Created by Joshua Sylvanus, 3:11 AM, 27-Jan-23.
 */
class MessageDialog(private val message:String,
                    private val prompt:String,
                    private val isCancellable:Boolean,
                    private val onClick:((MessageDialog) -> Unit)? = null) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme)
            .also {
                val dialogView = onCreateView(layoutInflater, null, savedInstanceState)
                dialogView?.let { it2 -> onViewCreated(it2, savedInstanceState) }
                it.setView(dialogView)
            }
            .create()
            .also{ it.setCanceledOnTouchOutside(isCancellable) }

    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view:View){
        view.findViewById<TextView>(R.id.tv_message).setText(message)
        with(view.findViewById<Button>(R.id.btn_ok)){
            text = prompt
            setOnClickListener {
                onClick?.invoke(this@MessageDialog)
                if(onClick == null)
                    this@MessageDialog.dismiss()
            }
        }
    }


}

