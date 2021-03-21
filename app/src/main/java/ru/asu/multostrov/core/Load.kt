package ru.asu.multostrov.core

import android.os.*
import android.content.*
import androidx.appcompat.app.AppCompatActivity

class Load {
    companion object {
        fun <T> nextActivity(sender: AppCompatActivity, next: T, delay: Long) where T : Class<*> {
            Handler().postDelayed({
                sender.startActivity(Intent(sender, next))

                sender.finish()
            }, delay)
        }

        fun <T> nextActivity(sender: AppCompatActivity, next: T) where T : Class<*> {
            sender.startActivity(Intent(sender, next))

            sender.finish()
        }
    }
}