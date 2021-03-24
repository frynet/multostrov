package ru.asu.multostrov.web

import org.jsoup.Jsoup
import android.webkit.*

class MainClient : WebViewClient() {
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest
    ): WebResourceResponse? {
        if (
            Config.pages.containsValue(request.url.lastPathSegment) ||
            request.url.pathSegments.contains("games")
        ) {
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

            val maincell = body.getElementById("maincell")
                ?: return super.shouldInterceptRequest(view, request)

            body.getElementById("printnone").remove()
            body.getElementById("batteryfaiconbot")?.parent()?.parent()?.remove()
            body.getElementById("body_1").attr(
                "style",
                "background: none !important; padding: 0 !important;"
            )

            maincell.attr(
                "style",
                "margin-bottom: 0 !important;"
            )

            maincell.parent().attr(
                "style",
                "padding: 0 !important;"
            )

            return WebResourceResponse(
                contentType,
                response.charset(),
                doc.outerHtml().byteInputStream()
            )
        }

        return super.shouldInterceptRequest(view, request)
    }
}