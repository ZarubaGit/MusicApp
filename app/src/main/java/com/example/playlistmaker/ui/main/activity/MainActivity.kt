package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.MediatekaActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.setting.activity.SettingsActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.buttonSearch)
        val mediaButton = findViewById<Button>(R.id.buttonMedia)
        val settingButton = findViewById<Button>(R.id.buttonSetting)

        searchButton.setOnClickListener {
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }
        mediaButton.setOnClickListener {
            val displayMedia = Intent(this, MediatekaActivity::class.java)
            startActivity(displayMedia)
        }
        settingButton.setOnClickListener {
            val displaySettings = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettings)
        }
    }
}