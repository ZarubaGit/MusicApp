package com.example.playlistmaker.ui.setting.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.setting.view_model_settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private  val  viewModel: SettingsViewModel by viewModel()//внедрение зависимостей с помощью DI и Koin

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
    }
}