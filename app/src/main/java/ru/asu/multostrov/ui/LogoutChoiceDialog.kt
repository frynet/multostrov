package ru.asu.multostrov.ui

import ru.asu.multostrov.R
import ru.asu.multostrov.core.Load
import ru.asu.multostrov.database.users.Users

import android.os.Bundle
import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class LogoutChoiceDialog(private val sender: MainActivity) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage(getString(R.string.logout_message))
                .setPositiveButton(getString(R.string.temporary_logout)) { _, _ ->
                    Users.logoutTemporary()
                    Load.nextActivity(sender, AuthorizationActivity::class.java)
                }
                .setNegativeButton(getString(R.string.permanent_logout)) { _, _ ->
                    Users.logoutPermanently()
                    Load.nextActivity(sender, AuthorizationActivity::class.java)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}