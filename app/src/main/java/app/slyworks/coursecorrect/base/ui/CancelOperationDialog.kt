package app.slyworks.coursecorrect.base.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import app.slyworks.coursecorrect.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * Created by Joshua Sylvanus, 3:11 AM, 14-Mar-23.
 */
class CancelOperationDialog() : DialogFragment() {
    private val resultFlow:MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme)
            .also {
                val dialogView = onCreateView(layoutInflater, null, savedInstanceState)
                dialogView?.let { it2 -> onViewCreated(it2, savedInstanceState) }
                it.setView(dialogView)
            }
            .create()
            .also{ it.setCanceledOnTouchOutside(false) }

    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_cancel_operation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view:View){
        view.findViewById<Button>(R.id.btn_ok).setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch{
                resultFlow.emit(true)
            }
        }
        view.findViewById<Button>(R.id.btn_wait).setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch{
                resultFlow.emit(false)
            }
        }
    }

    fun getResultFlow(): StateFlow<Boolean> = resultFlow

}

