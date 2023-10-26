package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setings)

        val buttonBack = findViewById<ImageView>(R.id.imageButton)

        buttonBack.setOnClickListener {
            val mainDisplay = Intent(this, MainActivity::class.java)
            startActivity(mainDisplay)
        }
    }
}