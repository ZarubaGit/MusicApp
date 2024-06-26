package com.example.playlistmaker.ui.main.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.ui.main.viewModelMain.RootViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity: AppCompatActivity() {

    private val viewModel: RootViewModel by viewModel()
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.setAppTheme()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.addPlayListFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.playlistDetailsFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.editPlayListFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.audioPlayerFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

    }

}