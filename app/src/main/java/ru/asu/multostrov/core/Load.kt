package ru.asu.multostrov.core

import android.os.*
import android.content.*
import androidx.appcompat.app.AppCompatActivity

class Load {
    companion object {
        fun nextActivity(
            sender: AppCompatActivity,
            next: Class<*>,
            delay: Long = 0
        ) {
            if (delay > 0) {
                Handler().postDelayed({
                    loadNext(sender, next)
                }, delay)
            } else {
                loadNext(sender, next)
            }
        }

        private fun loadNext(
            sender: AppCompatActivity,
            next: Class<*>
        ) {
            sender.startActivity(Intent(sender, next))

            sender.finish()
        }
    }
}