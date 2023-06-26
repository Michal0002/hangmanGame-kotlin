package com.example.hangman_the_game_ie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.example.hangman_the_game_ie.dbase.MyDatabaseHelper

class GameCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_category)
        val spinner: Spinner = findViewById(R.id.spinner_difficulty)
        val items = listOf("hard", "medium", "easy")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val username = intent.getStringExtra("username").toString()

        val dbHelper = MyDatabaseHelper(this)
        val coins = dbHelper.getCoinsForUser(username)
        val coinsy = findViewById<TextView>(R.id.textView16)
        coinsy.text = coins.toString()

        //powrót do menu
        val button_back = findViewById<Button>(R.id.button_goback)
        button_back.setOnClickListener{
            var intent = Intent(this, Hangman_main::class.java )
            intent.putExtra("username", username)
            intent.putExtra("coins", coins)
            startActivity(intent)
        }
        //zacznij gre
        val button_start = findViewById<Button>(R.id.button_start)
        button_start.setOnClickListener{
            val spinner_difficulty = findViewById<Spinner>(R.id.spinner_difficulty)
            val selectedDifficulty = spinner_difficulty.selectedItem.toString()

            var intent = Intent(this, GameActivity::class.java )
            intent.putExtra("username", username)
            intent.putExtra("coins", coins)
            intent.putExtra("selectedDifficulty", selectedDifficulty)
            startActivity(intent)
        }
    }
}