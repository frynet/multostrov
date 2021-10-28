package ru.asu.multostrov.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_user.*
import kotlinx.android.synthetic.main.pattern_login_new_user.*
import kotlinx.android.synthetic.main.pattern_password.*
import kotlinx.android.synthetic.main.pattern_password.view.*
import ru.asu.multostrov.R

class AddNewUserActivity : LoginActivity, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_user)

        loginButton.setOnClickListener {
            val login = new_user_login.text.toString()
            val password = password_field.text.toString()

            Authorization(this, login, password, remember_me.isChecked).execute()
        }
    }

    override fun clearFields() {
        pattern_password.password_field.text.clear()
    }
}