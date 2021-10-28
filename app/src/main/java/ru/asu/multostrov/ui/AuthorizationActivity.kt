package ru.asu.multostrov.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_authorization.*
import kotlinx.android.synthetic.main.pattern_login_ex_user.view.*
import kotlinx.android.synthetic.main.pattern_password.*
import kotlinx.android.synthetic.main.pattern_password.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.asu.multostrov.R
import ru.asu.multostrov.core.Load
import ru.asu.multostrov.database.users.Users

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

            CoroutineScope(Dispatchers.IO).launch {
                Authorization.run(
                    this@AuthorizationActivity,
                    login,
                    password,
                    remember_me.isChecked
                )
            }
        }

        login_to_another.setOnClickListener {
            Load.nextActivity(this, AddNewUserActivity::class.java)
        }
    }

    override fun clearFields() {
        pattern_password.password_field.text.clear()
    }
}