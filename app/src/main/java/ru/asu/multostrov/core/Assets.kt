package ru.asu.multostrov.core

import android.content.res.AssetManager

class Assets {
    companion object {
        private lateinit var assets: AssetManager

        fun connect(manager: AssetManager) {
            assets = manager
        }

        fun readFileAsStr(filename: String) = assets.open(filename).readBytes().toString(Charsets.UTF_8)
    }
}