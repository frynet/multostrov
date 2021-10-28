package ru.asu.multostrov.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_promo.*
import ru.asu.multostrov.R
import ru.asu.multostrov.web.Config
import ru.asu.multostrov.web.PromoClient

class PromoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo)

        setUpWebView()

        webView.loadUrl(Config.BASE_URL)

        getStarted.setOnClickListener {
            startActivity(Intent(this, AddNewUserActivity::class.java))

            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        webView.webViewClient = PromoClient()
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
    }
}