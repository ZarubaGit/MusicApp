package com.example.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {


    private lateinit var backButton: ImageView
    private lateinit var shareButton: ImageView
    private lateinit var switchTheme: Switch
    private lateinit var supportSend: ImageView
    private lateinit var userAccepted: ImageView
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        backButton = binding.backInMain
        shareButton = binding.buttonShare
        switchTheme = binding.switchTheme
        supportSend = binding.sendInSupport
        userAccepted = binding.userAccepted

        // Загрузка сохраненной темы
        val sharedPreference = getSharedPreferences(FILE_FOR_SAVED, MODE_PRIVATE)
        val darkTheme = sharedPreference.getBoolean(DARK_THEME_MODE, false)
        switchTheme.isChecked = darkTheme
        setAppTheme(darkTheme)

        shareButton.setOnClickListener {
            val shareButton = Intent(Intent.ACTION_SEND)
            shareButton.type = "text/plain"
            shareButton.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareSub))
            shareButton.putExtra(Intent.EXTRA_TEXT, getString(R.string.linkOnAndDevol))
            startActivity(Intent.createChooser(shareButton, getString(R.string.titleForShareIcon)))
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            switchTheme(isChecked)
            savedThemePreference(isChecked)
        }

        supportSend.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            val emailStudent = getString(R.string.emailMy)
            shareIntent.data = Uri.parse("mailto: $emailStudent")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.messageForEmailDevolp))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.messageForEmail))
            startActivity(shareIntent)
        }

        userAccepted.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.offerta))
            startActivity(intent)
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setAppTheme(darkThemeEnable: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnable) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun savedThemePreference(darkThemeEnable: Boolean) {
        val sharedPref = getSharedPreferences(FILE_FOR_SAVED, MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(DARK_THEME_MODE, darkThemeEnable)
            apply()
        }
    }

    private fun switchTheme(darkThemeEnable: Boolean) {
        setAppTheme(darkThemeEnable)
        savedThemePreference(darkThemeEnable)
    }
}