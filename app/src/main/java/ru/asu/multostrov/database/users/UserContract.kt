package ru.asu.multostrov.database.users

import android.provider.BaseColumns

object UserContract {
    object UserEntry : BaseColumns {
        const val ID = "id"
        const val TABLE_NAME = "users"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_SESSION_COOKIE = "PHPSESSID"

        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME(
                $ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LOGIN TEXT NOT NULL UNIQUE, 
                $COLUMN_PASSWORD TEXT,
                $COLUMN_SESSION_COOKIE TEXT
            );
        """

        const val DELETE_TABLE = """
            DROP TABLE IF EXISTS $TABLE_NAME;
        """

        const val CLEAR = """
           DELETE FROM $TABLE_NAME; 
        """
    }
}