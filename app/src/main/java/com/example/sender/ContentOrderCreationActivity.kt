package com.example.sender

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ContentOrderCreationActivity : AppCompatActivity() {
    private lateinit var orderButton: ImageButton
    private lateinit var profileImageView: ImageView
    private lateinit var imageButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_order_creation)


        orderButton = findViewById(R.id.orderButton)
        profileImageView = findViewById(R.id.profileImage)
        imageButton = findViewById(R.id.imageButton13)

        imageButton.setOnClickListener{
            val intent = Intent(this@ContentOrderCreationActivity, OrderListActivity::class.java )
            startActivity(intent)
        }

        profileImageView.setOnClickListener{
            val intent = Intent(this@ContentOrderCreationActivity, ProfileActivity::class.java)
            startActivity(intent)
        }


        orderButton.setOnClickListener {
            val intent = Intent(this@ContentOrderCreationActivity, OrderCreationActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Handle the settings menu item click here
                SettingsActivity() // You should create a function to open your settings activity
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}
