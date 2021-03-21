package ru.asu.multostrov.ui

import ru.asu.multostrov.R
import ru.asu.multostrov.web.*

import android.webkit.*
import android.view.View
import android.os.Bundle
import android.widget.ProgressBar
import android.annotation.SuppressLint
import androidx.core.view.*
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    object JSInterface {
        @JavascriptInterface
        fun callback() {
            showWebView()
        }
    }

    companion object {
        lateinit var instance: AppCompatActivity

        @SuppressLint("StaticFieldLeak")
        lateinit var webView: WebView

        @SuppressLint("StaticFieldLeak")
        lateinit var progressBar: ProgressBar

        fun hideWebView() {
            instance.runOnUiThread {
                if (webView.isVisible) {
                    webView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            }
        }

        fun showWebView() {
            instance.runOnUiThread {
                if (webView.isGone) {
                    webView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private val cookieManager = CookieManager.getInstance()

    init {
        cookieManager.acceptCookie()
        cookieManager.setCookie(Config.BASE_URL, WebManager.getCookie())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instance = this
        MainActivity.webView = webView
        MainActivity.progressBar = progressBar

        setUpWebView()

        progressBar.visibility = View.VISIBLE

        loadTab(TABS.LIB)

        lib.setOnClickListener {
            loadTab(TABS.LIB)
        }

        school.setOnClickListener {
            loadTab(TABS.SCHOOL)
        }

        games.setOnClickListener {
            loadTab(TABS.GAMES)
        }

        profile.setOnClickListener {
            loadTab(TABS.PROFILE)
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
        webView.visibility = View.GONE
        webView.webViewClient = MainClient()
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.addJavascriptInterface(JSInterface, "Android")
    }

    private fun loadTab(tab: TABS) {
        val url = Config.BASE_URL + Config.ref[tab]

        webView.loadUrl(url)
    }
}