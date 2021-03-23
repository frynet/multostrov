package ru.asu.multostrov.ui

import ru.asu.multostrov.R
import ru.asu.multostrov.core.*
import ru.asu.multostrov.database.users.Users
import ru.asu.multostrov.database.users.Users.UserState

import android.os.*
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {

    companion object {
        private const val DELAY_ACTIVITY: Long = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        Assets.connect(assets)
        CookieManager.getInstance().removeAllCookies(null)

        TryLogIn().execute()
    }

    private inner class TryLogIn : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            Users.connect(applicationContext)
        }

        override fun onPostExecute(result: Unit?) {
            when (Users.state()) {
                UserState.CAN_LOGIN -> Load.nextActivity(
                    this@LaunchActivity,
                    MainActivity::class.java,
                    DELAY_ACTIVITY
                )

                UserState.NEW_USER -> Load.nextActivity(
                    this@LaunchActivity,
                    PromoActivity::class.java,
                    DELAY_ACTIVITY
                )

                UserState.NOT_LOGGED_IN, UserState.NULL_PASSWORD -> Load.nextActivity(
                    this@LaunchActivity,
                    AuthorizationActivity::class.java,
                    DELAY_ACTIVITY
                )
            }
        }
    }
}