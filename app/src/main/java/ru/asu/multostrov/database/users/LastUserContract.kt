package ru.asu.multostrov.database.users

import android.provider.BaseColumns

object LastUserContract {
    object LastUserEntry : BaseColumns {
        const val ID = "id"
        const val TABLE_NAME = "last_user"

        const val CREATE_TABLE = """            
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $ID INTEGER
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