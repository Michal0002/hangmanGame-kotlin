package com.example.hangman_the_game_ie.dbase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast
import com.example.hangman_the_game_ie.WordListEasy
import com.example.hangman_the_game_ie.WordListHard
import com.example.hangman_the_game_ie.WordListMedium

class MyDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="Hangman_DB2"
        val DB_VERSION = 1
        val TABLE_USERS = "Users"
        val TABLE_WORDS = "Words"
        val COL_ID = "id"
        val COL_EMAIL = "email"
        val COL_PASSWORD = "password"
        val COL_USERNAME = "username"
        val COL_COINS = "coins"
        val COL_WORD_NAME = "name"
        val COL_WORD_DIFFICULT = "difficult"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUsers = "CREATE TABLE $TABLE_USERS ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_EMAIL TEXT, $COL_PASSWORD TEXT, $COL_USERNAME TEXT, $COL_COINS INTEGER)"
        val createTableWords = "CREATE TABLE $TABLE_WORDS ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_WORD_NAME TEXT, $COL_WORD_DIFFICULT TEXT)"

        if (db != null) {
            db.execSQL(createTableUsers)
            db.execSQL(createTableWords)

            //łatwe
            val wordsEasy = WordListEasy.getWords()
            for (word in wordsEasy) {
                val insertQueryWord = "INSERT INTO $TABLE_WORDS ($COL_WORD_NAME, $COL_WORD_DIFFICULT) VALUES ('$word', 'easy')"
                db.execSQL(insertQueryWord)
            }
            //średnie
            val wordsMedium = WordListMedium.getWords()
            for (word in wordsMedium) {
                val insertQueryWord = "INSERT INTO $TABLE_WORDS ($COL_WORD_NAME, $COL_WORD_DIFFICULT) VALUES ('$word', 'medium')"
                db.execSQL(insertQueryWord)
            }
            //trudne
            val wordsHard= WordListHard.getWords()
            for (word in wordsHard) {
                val insertQueryWord = "INSERT INTO $TABLE_WORDS ($COL_WORD_NAME, $COL_WORD_DIFFICULT) VALUES ('$word', 'hard')"
                db.execSQL(insertQueryWord)
            }
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    //Zarejestruj użytkownika
    fun addUser(username: String, password: String, email: String): Boolean {
        val db = this.writableDatabase

        val usernameExists = checkIfUsernameExists(db, username)
        val emailExists = checkIfEmailExists(db, email)

        if (usernameExists || emailExists) {
            Toast.makeText(context, "User already exists.", Toast.LENGTH_SHORT).show()
            return false
        }

        val values = ContentValues()
        values.put(COL_USERNAME, username)
        values.put(COL_PASSWORD, password)
        values.put(COL_EMAIL, email)
        values.put(COL_COINS, 100)
        val result = db.insert(TABLE_USERS, null, values)
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }

        db.close()
        return result != -1L
    }
    //Zaloguj użytkownika
    @SuppressLint("Range")
    fun loginUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf(COL_ID)
        val selection = "$COL_USERNAME = ? AND $COL_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }
    // Sprawdź czy istnieje użytkownik o podanym username w bazie danych
    private fun checkIfUsernameExists(db: SQLiteDatabase, username: String): Boolean {
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    private fun checkIfEmailExists(db: SQLiteDatabase, email: String): Boolean {
        val query = "SELECT * FROM $TABLE_USERS WHERE $COL_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    // Pobierz liczbę monet od użytkownika
    @SuppressLint("Range")
    fun getCoinsForUser(username: String): Int {
        val db = readableDatabase
        var coins = 0

        val query = "SELECT coins FROM users WHERE username = ?"
        val selectionArgs = arrayOf(username)

        val cursor = db.rawQuery(query, selectionArgs)

        if (cursor.moveToFirst()) {
            coins = cursor.getInt(cursor.getColumnIndex("coins"))
        }
        cursor.close()
        db.close()

        return coins
    }

    // Pobierz słówko z bazy danych
    @SuppressLint("Range")
    fun getRandomWord(difficulty: String): String? {
        val db = this.readableDatabase
        val query = "SELECT $COL_WORD_NAME FROM $TABLE_WORDS WHERE $COL_WORD_DIFFICULT = ? ORDER BY RANDOM() LIMIT 1"
        var randomWord: String? = null
        val cursor = db.rawQuery(query, arrayOf(difficulty))
        if (cursor.moveToFirst()) {
            randomWord = cursor.getString(cursor.getColumnIndex(COL_WORD_NAME))
        }
        cursor.close()
        db.close()
        return randomWord
    }
    fun addWord(name: String, difficulty: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_WORD_NAME, name)
        values.put(COL_WORD_DIFFICULT, difficulty)
        val result = db.insert(TABLE_WORDS, null, values)
        if (result == -1L) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
        db.close()
        return result != -1L
    }
    @SuppressLint("Range")
    fun getWords(): ArrayList<String> {
        val words = ArrayList<String>()
        val db = this.readableDatabase
        val query = "SELECT $COL_WORD_NAME, $COL_WORD_DIFFICULT FROM $TABLE_WORDS"
        val cursor: Cursor? = db.rawQuery(query, null)
    
        cursor?.let {
            val columnIndex = cursor.getColumnIndex(MyDatabaseHelper.COL_WORD_NAME)
            while (cursor.moveToNext()) {
                val name = cursor.getString(columnIndex)
                words.add(name)
            }
        }
        cursor?.close()
        db.close()
        return words
    }
    fun queryWithBuilder(projectionInColumns : Array<String>?, filterColumns: String?, filterValues : Array<String>?, order :String? ) : Cursor{
        var builder = SQLiteQueryBuilder()
        builder.tables = TABLE_WORDS
        val database = this.readableDatabase
        return builder.query (database,
            projectionInColumns,
            filterColumns,
            filterValues,
            null,
            null,
            order)
    }
    fun updateUserCoins(username: String, coins: Int) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_COINS, coins)
        db.update(TABLE_USERS, contentValues, "$COL_USERNAME = ?", arrayOf(username))
        db.close()
    }

    fun hintUserCoins(username: String, coins: Int) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_COINS, coins)
        db.update(TABLE_USERS, contentValues, "$COL_USERNAME = ?", arrayOf(username))
        db.close()
    }

}

