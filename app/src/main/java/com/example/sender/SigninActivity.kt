package com.example.sender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class SigninActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var registerText: TextView


    // Creating firebaseAuth object
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // View Binding
        btnLogin = findViewById(R.id.LoginButton)
        etEmail = findViewById(R.id.emailAddress)
        etPass = findViewById(R.id.passWord)
        registerText = findViewById(R.id.loginTextView1)

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login()
        }
        registerText.setOnClickListener {
            val intent = Intent(this@SigninActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // Initialize com.example.sender.SharedPreferencesHelper after the login button click
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val authStatus = sharedPreferencesHelper.getString("authStatus", "loggedOut")

        if (authStatus == "loggedIn") {
            val i = Intent(this@SigninActivity, ContentOrderCreationActivity::class.java)
            startActivity(i)
            finish() // Optional: Finish the SigninActivity to prevent going back to it
        } else {
            //
        }
    }


    private fun login() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()

        if (email.isBlank() || pass.isBlank()) {
            Toast.makeText(this, "Email and password must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val i = Intent(this@SigninActivity, ContentOrderCreationActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this, "Log In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}




