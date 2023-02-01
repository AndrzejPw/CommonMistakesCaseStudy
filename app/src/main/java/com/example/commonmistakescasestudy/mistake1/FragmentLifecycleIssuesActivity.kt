package com.example.commonmistakescasestudy.mistake1

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.commonmistakescasestudy.R

class FragmentLifecycleIssuesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}

class FragmentLifecycleIssuesFragment : Fragment(R.layout.fragment_lifecycle_issues) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.show_dialog_button).setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        CustomDialogFragment().apply {
            message = "dynamic message"
            callback = {
                println("do sth")
            }
        }.show(childFragmentManager, null)

    }
}

class CustomDialogFragment : DialogFragment() {
    var message = ""
    var callback = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).setTitle(message)
            .setPositiveButton("ok") { _, _ -> callback.invoke() }
            .create()
    }
}
