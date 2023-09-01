package com.example.sender

import com.google.firebase.database.PropertyName

data class User(
    val firstName: String = "",
    val lastName: String = "",
    @get:PropertyName("emailAddress") @set:PropertyName("emailAddress")
    var email: String = "",

    val phoneNumber: String = "",
    val password: String = ""
)

