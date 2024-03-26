package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.main.viewModelMain.MainViewModel
import com.example.playlistmaker.ui.media.MediatekaActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.setting.activity.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()//внедрение зависимостей с помощью DI и Koin
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.setAppTheme()

        binding.buttonSearch.setOnClickListener {
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }
        binding.buttonMedia.setOnClickListener {
            val displayMedia = Intent(this, MediatekaActivity::class.java)
            startActivity(displayMedia)
        }
        binding.buttonSetting.setOnClickListener {
            val displaySettings = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettings)
        }
    }
}