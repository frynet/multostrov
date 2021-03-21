package ru.asu.multostrov.web

object Config {

    const val BASE_URL = "http://multostrov.ru/"

    const val SESSION_COOKIE = "PHPSESSID"

    const val LOGIN_PARAM = "login"

    const val PASSWORD_PARAM = "pass"

    const val USER_AGENT = "Chrome"

    const val MAIN_CONTENT_STYLE = "mainContent.css"

    const val MAIN_CONTENT_SCRIPT = "mainContent.js"

    const val PROMO_CONTENT_STYLE = "promoContent.css"

    const val PROMO_CONTENT_SCRIPT = "promoContent.js"

    const val AUTH_REQUEST = "http://multostrov.ru/status.php?action=auth"

    const val NOT_AUTHORIZED = "NOT AUTHORIZED"

    const val AUTHORIZED = "USER ID:"

    val ref = mapOf(
        TABS.HOME to "index.php",
        TABS.LIB to "content.php",
        TABS.SCHOOL to "lessons.php",
        TABS.GAMES to "game.php",
        TABS.PROFILE to "profile.php"
    )
}