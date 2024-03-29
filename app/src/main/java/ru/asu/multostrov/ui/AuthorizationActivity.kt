package ru.asu.multostrov.ui

import ru.asu.multostrov.R
import ru.asu.multostrov.core.Load
import ru.asu.multostrov.database.users.Users

import android.os.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.pattern_password.*
import kotlinx.android.synthetic.main.pattern_password.view.*
import kotlinx.android.synthetic.main.activity_authorization.*
import kotlinx.android.synthetic.main.pattern_login_ex_user.view.*

class AuthorizationActivity : LoginActivity, AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        val listUsers = Users.getListUsers()
        val spinner = pattern_login.ex_user_login
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listUsers
        )

        loginButton.setOnClickListener {
            val login = spinner.selectedItem.toString()
            val password = password_field.text.toString()

            Authorization(this, login, password, remember_me.isChecked).execute()
        }

        login_to_another.setOnClickListener {
            Load.nextActivity(this, AddNewUserActivity::class.java)
        }
    }

    override fun clearFields() {
        pattern_password.password_field.text.clear()
    }
}