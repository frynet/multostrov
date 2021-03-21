package ru.asu.multostrov.web

import android.webkit.*

open class WebClient : WebViewClient() {
    protected lateinit var style: String
    protected lateinit var script: String

    override fun onLoadResource(view: WebView?, url: String?) {
        val code = """
            javascript:(function() { 
                var style_node = document.createElement('style');
                style_node.type = 'text/css';
                style_node.innerHTML = '$style';
                document.head.appendChild(style_node);
                
                $script
            })()
        """.trimIndent()

        view?.loadUrl(code)
    }
}