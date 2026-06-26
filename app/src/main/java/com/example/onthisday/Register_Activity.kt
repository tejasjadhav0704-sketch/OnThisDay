package com.example.onthisday

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class Register_Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = findViewById<EditText>(R.id.name_id)
        val email = findViewById<EditText>(R.id.email_id)
        val password = findViewById<EditText>(R.id.password_id)
        val birth = findViewById<EditText>(R.id.birth_id)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val nametext = name.text.toString().trim()
            val emailtext = email.text.toString().trim()
            val passwordtext = password.text.toString()
            val birthtext = birth.text.toString().trim()

            if (nametext.isEmpty() || emailtext.isEmpty() || passwordtext.isEmpty() || birthtext.isEmpty()) {
                if (nametext.isEmpty()) name.error = "Name is required"
                if (emailtext.isEmpty()) email.error = "Email is required"
                if (passwordtext.isEmpty()) password.error = "Password is required"
                if (birthtext.isEmpty()) birth.error = "Birth is required"
                return@setOnClickListener
            }
            else
            {
                FireBase_Instance.database.collection("Users_Data").document(nametext).get()
                    .addOnSuccessListener { docname ->
                        if (docname.exists())
                        {
                            name.error = "User-Name already exists"
                        }
                        else {
                            FireBase_Instance.auth.createUserWithEmailAndPassword(emailtext, passwordtext).addOnSuccessListener {
                                val data = data_storing(nametext, emailtext, passwordtext, birthtext)
                                FireBase_Instance.database.collection("Users_Data").document(nametext).set(data).addOnSuccessListener {
                                    val intent = Intent(this, Login_Activity::class.java)
                                    startActivity(intent)
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(this, "data not added", Toast.LENGTH_SHORT).show()
                                }

                            }.addOnFailureListener {
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "cant fetch document", Toast.LENGTH_SHORT).show()
                    }
            }


        }

        val alreadySigned = findViewById<TextView>(R.id.textView_1)
        alreadySigned.setOnClickListener {
            startActivity(Intent(this, Login_Activity::class.java))
        }
    }
}
