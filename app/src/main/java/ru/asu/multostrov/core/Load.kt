package ru.asu.multostrov.core

import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class Load {
    companion object {
        /**
         * Load next activity and finish current.
         *
         * @param sender the current activity.
         * @param next the next activity class.
         * @param delay (optional) delay before loading [next].
         */
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