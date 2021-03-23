package ru.asu.multostrov.ui

import ru.asu.multostrov.R
import ru.asu.multostrov.web.*

import android.os.Bundle
import android.content.Intent
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_promo.*

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