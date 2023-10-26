package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val clickButton = findViewById<Button>(R.id.button1)
        val clickButton2 = findViewById<Button>(R.id.button2)
        val clickButton3 = findViewById<Button>(R.id.button3)

        clickButton.setOnClickListener  {
            val displaySearch = Intent(this, searchActivity::class.java)
            startActivity(displaySearch)
        }
        clickButton2.setOnClickListener  {
            val displayMedia = Intent(this, mediatekaActivity::class.java)
            startActivity(displayMedia)
        }
        clickButton3.setOnClickListener  {
            val displaySettings = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettings)
        }

    }

}