package ru.asu.multostrov.ui

import ru.asu.multostrov.R
import ru.asu.multostrov.core.Load
import ru.asu.multostrov.database.users.Users
import ru.asu.multostrov.database.users.Users.UserState

import android.os.Bundle
import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.Exception

class LogoutChoiceDialog(private val sender: MainActivity) : DialogFragment() {

    private fun loadNext() {
        val state = Users.state()

        Load.nextActivity(
            sender, when (state) {
                UserState.NEW_USER -> AddNewUserActivity::class.java
                UserState.NOT_LOGGED_IN, UserState.NULL_PASSWORD -> AuthorizationActivity::class.java
                else -> throw Exception("Logout choice dialog: Unknown user state - $state")
            }
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage(getString(R.string.logout_message))
                .setPositiveButton(getString(R.string.temporary_logout)) { _, _ ->
                    Users.logoutTemporary()

                    loadNext()
                }
                .setNegativeButton(getString(R.string.permanent_logout)) { _, _ ->
                    Users.logoutPermanently()

                    loadNext()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}