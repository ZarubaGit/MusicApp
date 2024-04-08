package com.example.playlistmaker.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.ui.media.favoriteSongFragmentViewModel.FavoriteSongsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteSongsFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoriteSongsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        fun newInstance() = FavoriteSongsFragment()
    }
}