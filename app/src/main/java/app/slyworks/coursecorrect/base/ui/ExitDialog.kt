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
import kotlin.system.exitProcess


/**
 * Created by Joshua Sylvanus, 9:11 PM, 2/7/2022.
 */
class ExitDialog : DialogFragment() {
    private lateinit var btnCancel: Button
    private lateinit var btnExit:Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme).apply {
            val dialogView = onCreateView(LayoutInflater.from(requireContext()),null, savedInstanceState)
            dialogView?.let {
                onViewCreated(it,savedInstanceState)
            }
            setView(dialogView)
        }.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_exit, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view:View){
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnExit = view.findViewById(R.id.btn_exit)

        btnCancel.setOnClickListener { dismiss() }
        btnExit.setOnClickListener { exitProcess(0) }
    }
}