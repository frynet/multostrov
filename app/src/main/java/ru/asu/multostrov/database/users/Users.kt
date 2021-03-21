package ru.asu.multostrov.database.users

import ru.asu.multostrov.web.WebManager
import ru.asu.multostrov.database.users.UserContract.UserEntry
import ru.asu.multostrov.database.users.LastUserContract.LastUserEntry

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

object Users {

    enum class UserState {
        NEW_USER,
        CAN_LOGIN,
        NOT_LOGGED_IN,
        NULL_PASSWORD
    }

    private var count: Long = 0
    private var lastUserID: Long = 0
    private lateinit var database: SQLiteDatabase
    private lateinit var dbHelper: UsersDBHelper

    lateinit var state: UserState

    fun connect(context: Context) {
        dbHelper = UsersDBHelper(context)
        database = dbHelper.writableDatabase
        count = getCount()
        lastUserID = getLastUserID()
        state = determineState()

        if (state == UserState.CAN_LOGIN) {
            updateCookie()
        }
    }

    fun getListUsers(): MutableList<String> {
        val list = mutableListOf<String>()

        val cursor = database.query(
            UserEntry.TABLE_NAME,
            arrayOf(UserEntry.COLUMN_LOGIN),
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            list.add(cursor.getString(0))

            cursor.moveToNext()
        }

        cursor.close()

        return list
    }

    fun add(login: String, password: String) {
        val newID = database.insertWithOnConflict(
            UserEntry.TABLE_NAME,
            null,
            ContentValues().apply {
                put(UserEntry.COLUMN_LOGIN, login)
                put(UserEntry.COLUMN_PASSWORD, password)
            },
            SQLiteDatabase.CONFLICT_REPLACE
        )

        database.execSQL(LastUserEntry.CLEAR)

        database.insertWithOnConflict(
            LastUserEntry.TABLE_NAME,
            null,
            ContentValues().apply {
                put(LastUserEntry.ID, newID)
            },
            SQLiteDatabase.CONFLICT_REPLACE
        )

        count++
        lastUserID = newID
        updateCookie()
    }

    fun logoutTemporary() {
        val query = """
            UPDATE ${UserEntry.TABLE_NAME}
            SET ${UserEntry.COLUMN_PASSWORD} = NULL, ${UserEntry.COLUMN_SESSION_COOKIE} = NULL
            WHERE ${UserEntry.ID} = $lastUserID;
        """.trimIndent()

        database.execSQL(query)
    }

    fun logoutPermanently() {
        count -= database.delete(
            UserEntry.TABLE_NAME,
            "${UserEntry.ID} = ?",
            arrayOf(lastUserID.toString())
        )

        database.execSQL(LastUserEntry.CLEAR)
    }

    private fun determineState(): UserState {
        if (count == 0L) return UserState.NEW_USER

        if (lastUserID < 0) return UserState.NOT_LOGGED_IN

        if (nullPassword()) return UserState.NULL_PASSWORD

        return UserState.CAN_LOGIN
    }

    private fun getCount(): Long {
        val cursor = database.query(
            UserEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val result = cursor.count.toLong()

        cursor.close()

        return result
    }

    private fun getLastUserID(): Long {
        var result: Long = -1

        val cursor = database.query(
            LastUserEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToFirst()

        if (cursor.count > 0 && cursor.getType(0) != Cursor.FIELD_TYPE_NULL) {
            result = cursor.getLong(0)
        }

        cursor.close()

        return result
    }

    private fun updateCookie() {
        val cursor = database.query(
            UserEntry.TABLE_NAME,
            null,
            "${UserEntry.ID} = ?",
            arrayOf("$lastUserID"),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val user = cursor.getString(1)                                  // last session user
        var invalidCookie = false                                       // didn't pass the check
        val isCookieNull = cursor.getType(3) == Cursor.FIELD_TYPE_NULL  // means empty cookie field

        if (!isCookieNull) {
            val cookie = cursor.getString(3)
            invalidCookie = !WebManager.checkCookie(cookie)
        }

        if (isCookieNull || invalidCookie) {
            val password = cursor.getString(2)
            val newCookie = WebManager.getCookie(user, password)

            cursor.close()

            if (!WebManager.checkCookie(newCookie)) {
                throw Exception("An error occurred on the SERVER: Invalid login or password!")
            }

            val values = ContentValues().apply {
                put(UserEntry.COLUMN_SESSION_COOKIE, newCookie)
            }

            database.update(
                UserEntry.TABLE_NAME,
                values,
                "${UserEntry.COLUMN_LOGIN} = ?",
                arrayOf(user)
            )
        }
    }

    private fun nullPassword(): Boolean {
        val cursor = database.query(
            UserEntry.TABLE_NAME,
            null,
            "${UserEntry.ID} = ?",
            arrayOf("$lastUserID"),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val result = cursor.getType(2) == Cursor.FIELD_TYPE_NULL

        cursor.close()

        return result
    }
}
