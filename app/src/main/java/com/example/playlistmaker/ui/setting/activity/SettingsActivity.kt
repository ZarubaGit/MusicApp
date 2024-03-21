package com.example.playlistmaker.ui.setting.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.setting.view_model_settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private  val  viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            binding.switchTheme.isChecked = true
        }
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateThemeSettings(isChecked)
        }

        binding.buttonShare.setOnClickListener {
            viewModel.shareApp(
                url = getString(R.string.shareSub),
                title = getString(R.string.titleForShareIcon))
        }

        binding.sendInSupport.setOnClickListener {
            viewModel.openSupport(
                email = getString(R.string.emailMy),
                subject = getString(R.string.messageForEmailDevolp),
                text = getString(R.string.messageForEmail)
            )
        }

        binding.userAccepted.setOnClickListener {
            viewModel.openTerms(
                url = getString(R.string.offerta)
            )
        }

        binding.backInMain.setOnClickListener {
            this.finish()
        }
    }
}