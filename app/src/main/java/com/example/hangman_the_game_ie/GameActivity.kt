package com.example.hangman_the_game_ie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hangman_the_game_ie.dbase.MyDatabaseHelper

class GameActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var selectedDifficulty: String
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var randomWord: String
    private lateinit var hiddenWord: String
    private lateinit var lettersUsed: MutableList<Char>
    private var currentAttempt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        username = intent.getStringExtra("username").toString()
        selectedDifficulty = intent.getStringExtra("selectedDifficulty").toString()
        dbHelper = MyDatabaseHelper(this)

        val coins = dbHelper.getCoinsForUser(username)
        val coins_value = findViewById<TextView>(R.id.textView10)
        coins_value.text = coins.toString()

        val word = findViewById<TextView>(R.id.textView_word)
        lettersUsed = mutableListOf()

        initializeGame()

        val textViewLettersUsed = findViewById<TextView>(R.id.textView_lettersused)
        val editTextLetter = findViewById<EditText>(R.id.editTextText_letter)
        val imageViewAttempts = findViewById<ImageView>(R.id.imageView_attempts)
        val buttonGuess = findViewById<Button>(R.id.button_guess)

        buttonGuess.setOnClickListener {
            val guessedLetter = editTextLetter.text.toString().trim().singleOrNull()?.toUpperCase()
            if (guessedLetter != null && guessedLetter in 'A'..'Z') {
                var correctGuess = false
                if (guessedLetter in lettersUsed) {
                    Toast.makeText(this, "This letter has already been used.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    lettersUsed.add(guessedLetter)
                    textViewLettersUsed.text = lettersUsed.joinToString(" ")
                    for (i in randomWord.indices) {
                        if (randomWord[i].toUpperCase() == guessedLetter) {
                            val revealedWord = StringBuilder(hiddenWord)
                            revealedWord.setCharAt(i * 2, guessedLetter)
                            hiddenWord = revealedWord.toString()
                            word.text = hiddenWord
                            correctGuess = true
                        }
                    }
                    if (correctGuess) {
                        Toast.makeText(
                            this,
                            "Good job! The letter $guessedLetter is in the word.",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (!hiddenWord.contains('_')) {
                            dbHelper.updateUserCoins(username, coins + 50)
                            var intent = Intent(this, Hangman_main::class.java)
                            intent.putExtra("username", username)
                            intent.putExtra("coins", coins)
                            startActivity(intent)
                            Toast.makeText(
                                this,
                                "You won! You received 50 coins.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        currentAttempt++
                        if (currentAttempt <= 11) {
                            val attemptsLeft = findViewById<TextView>(R.id.textView_attemptsLeft)
                            val attempts = (11 - currentAttempt)
                            attemptsLeft.text =
                                ("Attempts left: $attempts").toString() // Aktualizacja pozostałych prób
                            val resourceName = "hangman$currentAttempt"
                            val resourceId =
                                resources.getIdentifier(resourceName, "drawable", packageName)
                            imageViewAttempts.setImageResource(resourceId)
                            Toast.makeText(this, "Incorrect letter.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Użytkownik przekroczył limit prób
                            // Dodaj odpowiednią logikę tutaj
                            Toast.makeText(this, "You lost.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Enter a single letter from A to Z.", Toast.LENGTH_SHORT)
                    .show()
            }
            editTextLetter.text.clear()
        }
        val buttonHint = findViewById<Button>(R.id.button_hint)
        var hintCount = 0

        buttonHint.setOnClickListener {
            if (hintCount < 3) {
                val coins = dbHelper.getCoinsForUser(username)
                if (coins >= 50) {
                    dbHelper.hintUserCoins(username, coins - 50)

                    val hintLetter = getRandomHintLetter()

                    val revealedIndices = mutableListOf<Int>()
                    for (i in randomWord.indices) {
                        if (randomWord[i].toUpperCase() == hintLetter && hiddenWord[i * 2] == '_') {
                            revealedIndices.add(i)
                        }
                    }

                        if (revealedIndices.isNotEmpty()) {
                            val revealedWord = StringBuilder(hiddenWord)
                            for (index in revealedIndices) {
                                revealedWord.setCharAt(index * 2, hintLetter)
                            }
                            hiddenWord = revealedWord.toString()
                            word.text = hiddenWord
                        } else {
                            Toast.makeText(this, "No more letters to reveal.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    editTextLetter.setText(hintLetter.toString())
                    hintCount++
                    val updatedCoins = coins - 50
                    val coinsValue = findViewById<TextView>(R.id.textView10)
                    coinsValue.text = updatedCoins.toString()
                } else {
                    Toast.makeText(this, "No enough coins.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "You have reached the hint limit.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Losujemy słówko z BD i zakrywamy je znakiem"_"
    private fun initializeGame() {
        randomWord = dbHelper.getRandomWord(selectedDifficulty) ?: "" //jeśli brak słowa zwracamy null
        hiddenWord = hideWord(randomWord)
        val word = findViewById<TextView>(R.id.textView_word)
//        val slowo = findViewById<TextView>(R.id.textView6)
        if (randomWord.isNotEmpty()) {
            word.text = hiddenWord.uppercase()
        } else {
            word.text = "No available words in the database."
        }
    }

    // Ukrywamy słowo znakiem - "_"
    private fun hideWord(word: String): String {
        val hidden = StringBuilder()  // obiekt StringBuilder dla ukrycia słowa
        for (i in word.indices) {  // iteracja po indeksach w słowie
            hidden.append("_ ")  // ukrywanie słowa
        }
        return hidden.toString()  // konwersja na strng
    }

    private fun getRandomHintLetter(): Char {
        val availableLetters = ('A'..'Z').filter { it !in lettersUsed && it in randomWord.toUpperCase() && it !in hiddenWord.toUpperCase() }
        return availableLetters.randomOrNull() ?: throw IllegalStateException("No more letters to reveal.")
    }
}
