package ru.asu.multostrov.ui

import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.coroutineScope
import ru.asu.multostrov.core.Load
import ru.asu.multostrov.database.users.Users
import ru.asu.multostrov.web.WebManager

object Authorization {

    private val quitApp = { sender: AppCompatActivity ->
        sender.finish()
        Process.killProcess(Process.myPid())
    }

    /**
     * Authorization of user
     *
     * @param T the type of sender's activity.
     * @property remember (optional) saving user for his next authorization without confirmation.
     */
    suspend fun <T> run(
        sender: T,
        login: String,
        password: String,
        remember: Boolean = false
    ) where T : AppCompatActivity, T : LoginActivity = coroutineScope {
        val cookie = WebManager.getSessionCookie(login, password)

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
            InvalidDataDialog<T>(sender, sender::clearFields, quitApp).show(
                sender.supportFragmentManager,
                "InvalidData"
            )
        }
    }
}