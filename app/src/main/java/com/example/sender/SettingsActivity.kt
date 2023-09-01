package com.example.sender

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switchCompat = findViewById<SwitchCompat>(R.id.switchCompat)



        if (!switchCompat.isChecked) {
            //

        } else {
            //
        }

    }
}
