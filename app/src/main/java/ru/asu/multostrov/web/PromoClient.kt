package ru.asu.multostrov.web

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import org.jsoup.Jsoup

class PromoClient : WebViewClient() {
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest
    ): WebResourceResponse? {

        if (request.url.toString().contains(Config.BASE_URL)) {
            val response = Jsoup
                .connect(request.url.toString())
                .cookies(WebManager.getCookies())
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()

            val contentType = response.contentType()

            if (!Config.allowedMimeTypes.contains(contentType)) {
                return super.shouldInterceptRequest(view, request)
            }

            val doc = response.parse()
            val body = doc.body()

            body.getElementsByClass("form-signin")[1].parent().parent().remove()

            return WebResourceResponse(
                contentType,
                response.charset(),
                doc.outerHtml().byteInputStream()
            )
        }

        return super.shouldInterceptRequest(view, request)
    }
}