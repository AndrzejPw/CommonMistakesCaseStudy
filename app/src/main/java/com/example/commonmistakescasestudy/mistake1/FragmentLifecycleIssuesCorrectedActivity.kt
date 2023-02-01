package com.example.commonmistakescasestudy.mistake1

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.commonmistakescasestudy.R

class FragmentLifecycleIssuesCorrectedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class FragmentLifecycleIssuesCorrectedFragment : Fragment(R.layout.fragment_lifecycle_issues) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.show_dialog_button).setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        CustomDialogCorrectedFragment.newInstance("dynamic message")
    }

}

class CustomDialogCorrectedFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).setTitle(requireArguments().getString("msg_arg", ""))
            .setPositiveButton("ok") { _, _ -> setFragmentResult("requestKey", Bundle()) }
            .create()
    }

    companion object {
        fun newInstance(message: String): CustomDialogCorrectedFragment {
            val args = Bundle()
            args.putString("msg_arg", message)
            val fragment = CustomDialogCorrectedFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
