package com.example.hangman_the_game_ie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.hangman_the_game_ie.dbase.MyDatabaseHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonSignIn = findViewById<Button>(R.id.button_signIn)
        buttonSignIn.setOnClickListener{
            val username = findViewById<EditText>(R.id.editTextText_username1).text.toString()
            val password = findViewById<EditText>(R.id.editTextText_password).text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = MyDatabaseHelper(this)
            val loggedIn = dbHelper.loginUser(username, password)

            if (loggedIn) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Hangman_main::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonSignUp = findViewById<Button>(R.id.button_registration)
        buttonSignUp.setOnClickListener{
            var intent = Intent(this, RegisterActivity::class.java )
            startActivity(intent)
        }
        val buttonGoogle = findViewById<ImageButton>(R.id.button_loginGoogle)
        buttonGoogle.setOnClickListener{
            Toast.makeText(this, "Google account", Toast.LENGTH_SHORT).show()
        }
        val buttonFacebook = findViewById<ImageButton>(R.id.button_loginFacebook)
        buttonFacebook.setOnClickListener{
            Toast.makeText(this, "Facebook account", Toast.LENGTH_SHORT).show()
        }
    }
}