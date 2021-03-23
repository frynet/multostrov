package ru.asu.multostrov.ui

import ru.asu.multostrov.core.Load
import ru.asu.multostrov.web.WebManager
import ru.asu.multostrov.database.users.Users

import android.os.*
import androidx.appcompat.app.AppCompatActivity

class Authorization<T>(
    private val sender: T,
    private val login: String,
    private val password: String,
    private val remember: Boolean = false
) : AsyncTask<Void, Void, Unit>() where T : AppCompatActivity, T : LoginActivity {

    private val quitApp = {
        sender.finish()
        Process.killProcess(Process.myPid())
    }

    override fun doInBackground(vararg p0: Void?) {
        val cookie = WebManager.getCookie(login, password)

        if (WebManager.checkCookie(cookie)) {
            if (remember) {
                when (sender) {
                    is AddNewUserActivity -> Users.add(login, password, cookie)
                    is AuthorizationActivity -> Users.update(login, password, cookie)
                    else -> throw Exception("Not allowed sender: ${sender::class.java.kotlin.simpleName}")
                }
            }

            Load.nextActivity(sender, MainActivity::class.java)
        } else {
            InvalidDataDialog<T>(sender::clearFields, quitApp).show(
                sender.supportFragmentManager,
                "InvalidData"
            )
        }
    }
}