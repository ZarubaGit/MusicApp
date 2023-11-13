package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var shareButton: ImageView
    private lateinit var switchTheme: Switch
    private lateinit var supportSend: ImageView
    private lateinit var userAccepted: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        backButton = findViewById<ImageView>(R.id.backInMain)
        shareButton = findViewById<ImageView>(R.id.buttonShare)
        switchTheme = findViewById<Switch>(R.id.switchTheme)
        supportSend = findViewById<ImageView>(R.id.sendInSupport)
        userAccepted = findViewById<ImageView>(R.id.userAccepted)

        shareButton.setOnClickListener {
            val shareButton = Intent(Intent.ACTION_SEND)
            shareButton.type = "text/plain"
            shareButton.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareSub))
            shareButton.putExtra(Intent.EXTRA_TEXT, getString(R.string.linkOnAndDevol))
            startActivity(Intent.createChooser(shareButton, getString(R.string.titleForShareIcon)))
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
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
}