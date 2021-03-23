package ru.asu.multostrov.ui

import ru.asu.multostrov.R

import android.os.Bundle
import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AppCompatActivity

class InvalidDataDialog<T>(
    private val positiveAction: () -> Unit,
    private val negativeAction: () -> Unit
) : DialogFragment() where T : AppCompatActivity {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage(R.string.invalid_data_dialog)
                .setPositiveButton(R.string.apply) { _, _ ->
                    positiveAction()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    negativeAction()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}