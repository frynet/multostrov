package ru.asu.multostrov.database.users

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.asu.multostrov.database.users.LastUserContract.LastUserEntry
import ru.asu.multostrov.database.users.UserContract.UserEntry
import ru.asu.multostrov.database.users.Users.UserState.*
import ru.asu.multostrov.web.WebManager

object Users {

    /**
     * Represents the user states.
     *
     * [NEW_USER] - the 'Users' table is empty (new user on device).
     *
     * [CAN_LOGIN] - user can log in service.
     *
     * [NOT_LOGGED_IN] - the last session user isn't saved.
     *
     * [NULL_PASSWORD] - the last session user need a confirmation of his password.
     */
    enum class UserState {
        NEW_USER,
        CAN_LOGIN,
        NOT_LOGGED_IN,
        NULL_PASSWORD
    }

    private var count: Long = 0
    private var lastUserID: Long = -1
    private lateinit var database: SQLiteDatabase
    private lateinit var dbHelper: UsersDBHelper

    fun connect(context: Context) {
        dbHelper = UsersDBHelper(context)
        database = dbHelper.writableDatabase
        count = getCount()
        lastUserID = getLastUserID()

        if (state() == CAN_LOGIN) {
            updateCookie()
        }
    }

    /**
     * The list of users that ever login from that device.
     *
     * @return [MutableList]
     */
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

    fun add(login: String, password: String, cookie: String) {
        count++

        val newID = database.insertWithOnConflict(
            UserEntry.TABLE_NAME,
            null,
            ContentValues().apply {
                put(UserEntry.COLUMN_LOGIN, login)
                put(UserEntry.COLUMN_PASSWORD, password)
                put(UserEntry.COLUMN_SESSION_COOKIE, cookie)
            },
            SQLiteDatabase.CONFLICT_NONE
        )

        updateLastSessionID(newID)
    }

    fun update(login: String, password: String, cookie: String) {
        database.update(
            UserEntry.TABLE_NAME,
            ContentValues().apply {
                put(UserEntry.COLUMN_PASSWORD, password)
                put(UserEntry.COLUMN_SESSION_COOKIE, cookie)
            },
            "${UserEntry.COLUMN_LOGIN} = ?",
            arrayOf(login)
        )

        updateLastSessionID(getIdByLogin(login))
    }

    /**
     * Logout from session, but still staying on device.
     */
    fun logoutTemporary() {
        if (state() == NOT_LOGGED_IN) return

        val query = """
            UPDATE ${UserEntry.TABLE_NAME}
            SET ${UserEntry.COLUMN_PASSWORD} = NULL, ${UserEntry.COLUMN_SESSION_COOKIE} = NULL
            WHERE ${UserEntry.ID} = $lastUserID;
        """.trimIndent()

        database.execSQL(query)
    }

    /**
     * Logout from session and drop account from device.
     */
    fun logoutPermanently() {
        if (state() == NOT_LOGGED_IN) return

        count -= database.delete(
            UserEntry.TABLE_NAME,
            "${UserEntry.ID} = ?",
            arrayOf(lastUserID.toString())
        )

        lastUserID = -1
        database.execSQL(LastUserEntry.CLEAR)
    }

    /**
     * Returns current user state.
     *
     * @return [UserState]
     */
    fun state(): UserState {
        if (count == 0L) return NEW_USER

        if (lastUserID < 0) return NOT_LOGGED_IN

        if (nullPassword()) return NULL_PASSWORD

        return CAN_LOGIN
    }

    private fun getIdByLogin(login: String): Long {
        val cursor = database.query(
            UserEntry.TABLE_NAME,
            arrayOf(UserEntry.ID),
            "${UserEntry.COLUMN_LOGIN} = ?",
            arrayOf(login),
            null,
            null,
            null
        )

        if (cursor.count == 0) {
            cursor.close()
            return -1L
        }

        cursor.moveToFirst()
        val result = cursor.getLong(0)
        cursor.close()

        return result
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

    private fun updateLastSessionID(id: Long) {
        database.execSQL(LastUserEntry.CLEAR)

        database.insertWithOnConflict(
            LastUserEntry.TABLE_NAME,
            null,
            ContentValues().apply {
                put(LastUserEntry.ID, id)
            },
            SQLiteDatabase.CONFLICT_REPLACE
        )

        lastUserID = id
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
            val newCookie = WebManager.getSessionCookie(user, password)

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
            arrayOf(UserEntry.COLUMN_PASSWORD),
            "${UserEntry.ID} = ?",
            arrayOf("$lastUserID"),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val result = cursor.getType(0) == Cursor.FIELD_TYPE_NULL

        cursor.close()

        return result
    }
}
