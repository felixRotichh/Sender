package com.example.sender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin = findViewById(R.id.LoginButton)
        btnRegister = findViewById(R.id.registerButton)


        btnRegister.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this@MainActivity, SigninActivity::class.java))


        }






    }
}
