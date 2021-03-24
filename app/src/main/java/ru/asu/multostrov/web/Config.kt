package ru.asu.multostrov.web

object Config {

    const val BASE_URL = "http://multostrov.ru/"

    const val SESSION_COOKIE = "PHPSESSID"

    const val LOGIN_PARAM = "login"

    const val PASSWORD_PARAM = "pass"

    const val USER_AGENT = "Chrome"

    const val AUTH_REQUEST = "http://multostrov.ru/status.php?action=auth"

    const val NOT_AUTHORIZED = "NOT AUTHORIZED"

    const val AUTHORIZED = "USER ID:"

    val allowedMimeTypes = listOf(
        "text/html"
    )

    val pages = mapOf(
        PAGE.LIB to "content.php",
        PAGE.SCHOOL to "lessons.php",
        PAGE.GAMES to "game.php",
        PAGE.PROFILE to "profile.php",
        PAGE.PAYMENT to "payment.php"
    )
}