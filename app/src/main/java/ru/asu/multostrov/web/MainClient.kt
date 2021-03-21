package ru.asu.multostrov.web

import ru.asu.multostrov.ui.MainActivity

import android.webkit.WebView
import android.graphics.Bitmap
import ru.asu.multostrov.core.Assets

class MainClient : WebClient() {
    init {
        style = Assets.readFileAsStr(Config.MAIN_CONTENT_STYLE)
        script = Assets.readFileAsStr(Config.MAIN_CONTENT_SCRIPT)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        MainActivity.hideWebView()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        val code = """
            javascript:(function() {                 
                Android.callback();
            })()
        """.trimIndent()

        view?.loadUrl(code)
    }
}