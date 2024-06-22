package com.example.playlistmaker.ui.media

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.activity.AudioPlayer
import com.example.playlistmaker.ui.media.favoriteSongFragmentViewModel.FavoriteSongsFragmentViewModel
import com.example.playlistmaker.ui.media.favoriteSongFragmentViewModel.FavoriteState
import com.example.playlistmaker.ui.search.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteSongsFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoriteSongsFragmentViewModel by viewModel()

    private var isClickAllowed = true
    private val trackAdapter = TrackAdapter {
            switchToPlayer(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observerFavoriteState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.recyclerFavorites.adapter = trackAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTracks()
    }

    private fun render(state: FavoriteState) {
        when(state) {
            is FavoriteState.Empty -> showEmpty()
            is FavoriteState.Content -> showContent(state.track)
        }
    }

    private fun showEmpty() {
        binding.media.visibility = View.VISIBLE
        binding.recyclerFavorites.visibility = View.GONE
    }

    private fun showContent(track: List<Track>) {
        binding.media.visibility = View.GONE

        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(track)
        trackAdapter.notifyDataSetChanged()

        binding.recyclerFavorites.visibility = View.VISIBLE
    }

    private fun switchToPlayer(track: Track) {
        if (clickDebounce()) {
            val displayIntent = Intent(requireContext(), AudioPlayer::class.java)
            displayIntent.putExtra("track", track)
            startActivity(displayIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(): FavoriteSongsFragment{
            return FavoriteSongsFragment()
        }

    }
}