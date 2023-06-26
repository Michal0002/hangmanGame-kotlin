package com.example.hangman_the_game_ie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.example.hangman_the_game_ie.dbase.MyDatabaseHelper

class AllWordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_words)
        val username = intent.getStringExtra("username").toString()
        val coins = intent.getStringExtra("coins").toString()


        val dbHelper = MyDatabaseHelper(this)

        val listOfAllWords = findViewById<ListView>(R.id.listView_AllWords)
        val words = dbHelper.getWords()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, words)
        listOfAllWords.adapter = adapter

        val nameFilter = findViewById<EditText>(R.id.editTextText_nameOfWord)
        val buttonBack = findViewById<Button>(R.id.button_back)
        buttonBack.setOnClickListener{
            val intent = Intent(this, Hangman_main::class.java)
            intent.putExtra("username", username)
            intent.putExtra("coins", coins)
            startActivity(intent)
        }
    }

}