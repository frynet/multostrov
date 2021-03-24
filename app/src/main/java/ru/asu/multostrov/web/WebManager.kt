package ru.asu.multostrov.web

import org.jsoup.*

object WebManager {

    private val cookies = mutableMapOf<String, String>()

    private fun setCookie(cookie: String) {
        cookies[Config.SESSION_COOKIE] = cookie
    }

    fun checkCookie(cookie: String): Boolean {
        setCookie(cookie)

        val doc = Jsoup
            .connect(Config.AUTH_REQUEST)
            .cookies(cookies)
            .get()

        val response = doc.body().childNode(0).toString()
        val regex1 = "\\s*${Config.AUTHORIZED}[\\s]{1}\\d+\\s*".toRegex()
        val regex2 = "\\s*${Config.NOT_AUTHORIZED}\\s*".toRegex()

        if (regex1.containsMatchIn(response)) {
            return true
        }

        if (regex2.containsMatchIn(response)) {
            return false
        }

        throw Exception("Can't validate cookie in function checkCookie: unknown response from ${Config.AUTH_REQUEST}")
    }

    fun getSessionCookie(login: String, password: String): String {
        val response = Jsoup
            .connect(Config.BASE_URL)
            .data(Config.LOGIN_PARAM, login)
            .data(Config.PASSWORD_PARAM, password)
            .userAgent(Config.USER_AGENT)
            .method(Connection.Method.POST)
            .execute()

        val cookie = response.cookie(Config.SESSION_COOKIE)

        setCookie(cookie)

        return cookie
    }

    fun getCookies(): Map<String, String> {
        return mapOf(
            Config.SESSION_COOKIE to cookies[Config.SESSION_COOKIE].toString()
        )
    }
}