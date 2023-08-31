package com.example.sender

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ContentOrderCreationActivity : AppCompatActivity() {
    private lateinit var orderButton: ImageButton
    private lateinit var profileImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_order_creation)


        orderButton = findViewById(R.id.orderButton)
        profileImageView = findViewById(R.id.profileImage)

        profileImageView.setOnClickListener{
            val intent = Intent(this@ContentOrderCreationActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        orderButton.setOnClickListener {
            val intent = Intent(this@ContentOrderCreationActivity, OrderCreationActivity::class.java)
            startActivity(intent)
        }
    }

}
