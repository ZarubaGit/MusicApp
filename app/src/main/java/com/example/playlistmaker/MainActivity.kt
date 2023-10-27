package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val clickButton = findViewById<Button>(R.id.buttonSearch)
        val clickButton2 = findViewById<Button>(R.id.buttonMedia)
        val clickButton3 = findViewById<Button>(R.id.buttonSetting)

        clickButton.setOnClickListener  {
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }
        clickButton2.setOnClickListener  {
            val displayMedia = Intent(this, MediatekaActivity::class.java)
            startActivity(displayMedia)
        }
        clickButton3.setOnClickListener  {
            val displaySettings = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettings)
        }

    }

}