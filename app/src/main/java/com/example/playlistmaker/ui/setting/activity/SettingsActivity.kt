package com.example.playlistmaker.ui.setting.activity

import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.view_model_settings.SettingsViewModel

class SettingsActivity : AppCompatActivity() {


    private lateinit var backButton: ImageView
    private lateinit var shareButton: ImageView
    private lateinit var supportSend: ImageView
    private lateinit var userAccepted: ImageView
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        backButton = binding.backInMain
        shareButton = binding.buttonShare
        supportSend = binding.sendInSupport
        userAccepted = binding.userAccepted

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]


        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            binding.switchTheme.isChecked = true
        }

        shareButton.setOnClickListener {
            viewModel.shareApp(
                url = getString(R.string.shareSub),
                title = getString(R.string.titleForShareIcon))
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateThemeSettings(isChecked)
        }

        supportSend.setOnClickListener {
            viewModel.openSupport(
                email = getString(R.string.emailMy),
                subject = getString(R.string.messageForEmailDevolp),
                text = getString(R.string.messageForEmail)
            )
        }

        userAccepted.setOnClickListener {
            viewModel.openTerms(
                url = getString(R.string.offerta)
            )
        }

        backButton.setOnClickListener {
            this.finish()
        }
    }
}