package ru.asu.multostrov.ui

import ru.asu.multostrov.R

import android.os.Bundle
import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class InvalidDataDialog(private val sender: AuthorizationActivity) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage(R.string.invalid_data_dialog)
                .setPositiveButton(R.string.apply) { _, _ ->
                    sender.clearFields()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    sender.finish()
                    System.exit(0)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}