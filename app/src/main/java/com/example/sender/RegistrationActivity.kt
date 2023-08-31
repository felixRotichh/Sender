package com.example.sender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegistrationActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
    private lateinit var fname: TextView
    private lateinit var lname: TextView
    private lateinit var phoneNumber: EditText


    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // View Bindings
        etEmail = findViewById(R.id.emailAddress)
        etConfPass = findViewById(R.id.confPassword)
        etPass = findViewById(R.id.passWord)
        btnSignUp = findViewById(R.id.registrationButton)
        fname = findViewById(R.id.firstName)
        lname = findViewById(R.id.secondName)
        phoneNumber = findViewById(R.id.phoneNumber)


        // Initialising auth object
        auth = Firebase.auth

        btnSignUp.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()
        val phoneNumberValue = phoneNumber.text.toString()
        val firstNameValue = fname.text.toString()
        val lastNameValue = lname.text.toString()

        // Check if fields are empty
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank() || phoneNumberValue.isBlank() || firstNameValue.isBlank() || lastNameValue.isBlank()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if password matches confirm password
        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Create user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                // If the user is authenticated successfully, save additional details to the database
                if (user != null) {
                    val userId = user.uid
                    val userReference =
                        FirebaseDatabase.getInstance().reference.child("users").child(userId)

                    // Create a HashMap to store user details
                    val userDetails = HashMap<String, Any>()
                    userDetails["firstName"] = firstNameValue
                    userDetails["lastName"] = lastNameValue
                    userDetails["emailAddress"] = email
                    userDetails["phoneNumber"] = phoneNumberValue
                    userDetails["password"] = pass

                    userReference.setValue(userDetails).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            val intent =
                                Intent(this@RegistrationActivity, SigninActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Database operation failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Sign Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
