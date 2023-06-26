package com.example.hangman_the_game_ie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import com.example.hangman_the_game_ie.dbase.MyDatabaseHelper

class RegisterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val dbHelper = MyDatabaseHelper(this)

        val back_button = findViewById<Button>(R.id.button2_back)
        back_button.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java )
            startActivity(intent)
        }

        val sign_up_button = findViewById<Button>(R.id.button_registration)
        sign_up_button.setOnClickListener {
            val usernameEditText = findViewById<EditText>(R.id.editText_username_signup)
            val passwordEditText = findViewById<EditText>(R.id.editText_password_signup)
            val passwordConfirmationEditText = findViewById<EditText>(R.id.editText_passwordconfirmation_signup)
            val emailEditText = findViewById<EditText>(R.id.editText_email)

                //usuwanie białych znaków metodą .trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val passwordConfirmation = passwordConfirmationEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            } else if (password == passwordConfirmation) {
                val isUserAdded = dbHelper.addUser(username, password, email)
                if (isUserAdded) {
                    Toast.makeText(this, "Your account has been created successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Password and password confirmation do not match.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}