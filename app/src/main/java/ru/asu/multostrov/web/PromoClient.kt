package ru.asu.multostrov.web

import ru.asu.multostrov.core.Assets

class PromoClient : WebClient() {
    init {
        style = Assets.readFileAsStr(Config.PROMO_CONTENT_STYLE)
        script = Assets.readFileAsStr(Config.PROMO_CONTENT_SCRIPT)
    }
}