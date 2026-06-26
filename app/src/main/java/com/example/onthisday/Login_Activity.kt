package com.example.onthisday

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login_Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (FireBase_Instance.auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val email = findViewById<EditText>(R.id.email_id)
        val password = findViewById<EditText>(R.id.password_id)
        val button = findViewById<Button>(R.id.button2)

        button.setOnClickListener {
            val emailtext = email.text.toString()
            val passwordtext = password.text.toString()

            if (emailtext.isEmpty() && passwordtext.isEmpty())
            {
                email.error = "Email is required"
                password.error = "Password is required"
            }
            else
            {
                FireBase_Instance.auth.signInWithEmailAndPassword(emailtext,passwordtext).addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        val donthaveaccount = findViewById<TextView>(R.id.donthaveAccount)
        donthaveaccount.setOnClickListener {
            val intent = Intent(this, Register_Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}