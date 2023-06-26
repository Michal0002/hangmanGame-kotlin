package com.example.hangman_the_game_ie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.hangman_the_game_ie.dbase.MyDatabaseHelper

class AdNewWordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_new_word)
        val username = intent.getStringExtra("username").toString()
        val coins = intent.getStringExtra("coins").toString()

        val spinner: Spinner = findViewById(R.id.spinner_difficultyNewWord)
        val items = listOf("hard", "medium", "easy")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val newWordName = findViewById<EditText>(R.id.editTextText_wordName)
        val selectedDifficult = findViewById<Spinner>(R.id.spinner_difficultyNewWord)
        val buttonAddNewWord = findViewById<Button>(R.id.button_addNewWord)

        buttonAddNewWord.setOnClickListener{
            val word = newWordName.text.toString()
            val difficult = selectedDifficult?.selectedItem?.toString()

            if (word.isNotEmpty() && difficult != null ) {
                val dbHelper = MyDatabaseHelper(this)
                val newWord = dbHelper.addWord(word, difficult)

                if (newWord) {
                    Toast.makeText(this, "Added new word to database: $word, $difficult", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter values for a new word and difficulty", Toast.LENGTH_SHORT).show()
            }
        }
    val buttonBack = findViewById<Button>(R.id.button_goBack)
        buttonBack.setOnClickListener{
            val intent = Intent(this, Hangman_main::class.java)
            intent.putExtra("username", username)
            intent.putExtra("coins", coins)
            startActivity(intent)

        }
    }
}