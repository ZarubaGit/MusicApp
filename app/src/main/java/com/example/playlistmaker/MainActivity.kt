package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.buttonSearch)
        val mediaButton = findViewById<Button>(R.id.buttonMedia)
        val settingButton = findViewById<Button>(R.id.buttonSetting)

        searchButton.setOnClickListener  {
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }
        mediaButton.setOnClickListener  {
            val displayMedia = Intent(this, MediatekaActivity::class.java)
            startActivity(displayMedia)
        }
        settingButton.setOnClickListener  {
            val displaySettings = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettings)
        }

    }

}