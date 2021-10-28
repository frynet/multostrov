package ru.asu.multostrov.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.asu.multostrov.R
import ru.asu.multostrov.web.Config
import ru.asu.multostrov.web.MainClient
import ru.asu.multostrov.web.PAGE

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpWebView()

        loadTab(PAGE.LIB)

        lib.setOnClickListener {
            loadTab(PAGE.LIB)
        }

        school.setOnClickListener {
            loadTab(PAGE.SCHOOL)
        }

        games.setOnClickListener {
            loadTab(PAGE.GAMES)
        }

        profile.setOnClickListener {
            loadTab(PAGE.PROFILE)
        }

        logout_btn.setOnClickListener {
            LogoutChoiceDialog(this).show(
                supportFragmentManager,
                "Logout"
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        webView.webViewClient = MainClient()
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
    }

    private fun loadTab(tab: PAGE) {
        val url = Config.BASE_URL + Config.pages[tab]

        webView.loadUrl(url)
    }
}