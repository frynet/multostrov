package ru.asu.multostrov.ui

import ru.asu.multostrov.R
import ru.asu.multostrov.core.*
import ru.asu.multostrov.database.users.Users
import ru.asu.multostrov.database.users.Users.UserState
import ru.asu.multostrov.web.WebManager.AuthStatus
import ru.asu.multostrov.web.WebManager.checkCookie

import android.os.*
import android.widget.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.pattern_password.*
import kotlinx.android.synthetic.main.pattern_password.view.*
import kotlinx.android.synthetic.main.activity_authorization.*
import kotlinx.android.synthetic.main.pattern_login_ex_user.*
import kotlinx.android.synthetic.main.pattern_login_ex_user.view.*
import kotlinx.android.synthetic.main.pattern_login_new_user.view.*

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var loginView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        createLoginView()

        loginButton.setOnClickListener {
            val login = when (Users.state) {
                UserState.NEW_USER -> {
                    loginView.new_user_login.text.toString()
                }

                else -> loginView.ex_user_login.selectedItem.toString()
            }

            val password = password_field.text.toString()

            Authorization(login, password).execute()
        }
    }

    fun clearFields() {
        if (Users.state == UserState.NEW_USER) {
            loginView.new_user_login.text.clear()
        }

        pattern_password.password_field.text.clear()
    }

    private fun createLoginView() {
        if (Users.state == UserState.NEW_USER) {
            pattern_login.layoutResource = R.layout.pattern_login_new_user
            loginView = pattern_login.inflate()
        } else {
            pattern_login.layoutResource = R.layout.pattern_login_ex_user
            loginView = pattern_login.inflate()

            val listUsers = Users.getListUsers()
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listUsers)
            ex_user_login.adapter = adapter
        }
    }

    private inner class Authorization(private var login: String, private var password: String) :
        AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg p0: Void?) {
            when (checkCookie(login, password)) {
                AuthStatus.YES -> {
                    if (remember_me.isChecked) {
                        Users.add(login, password)
                    }

                    Load.nextActivity(this@AuthorizationActivity, MainActivity::class.java)
                }
                AuthStatus.NO -> {
                    InvalidDataDialog(this@AuthorizationActivity).show(
                        supportFragmentManager,
                        "InvalidData"
                    )
                }
            }
        }
    }
}