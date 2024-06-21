package com.example.playlistmaker.ui.audioPlayer.activity


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.DpToPx
import com.example.playlistmaker.ui.audioPlayer.PlayListAdapter
import com.example.playlistmaker.ui.audioPlayer.view_model_audio_player.AudioPlayerViewModel
import com.example.playlistmaker.ui.media.addPlayList.AddPlayListFragment
import com.example.playlistmaker.ui.media.playList.PlayListState
import com.example.playlistmaker.ui.media.playList.PlayListTrackState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayer : AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by viewModel()
    private lateinit var artworkImageView: ImageView
    private lateinit var binding: ActivityAudioPlayerBinding
    private val formatYear by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }
    private val convert = DpToPx()
    private lateinit var savedTimeTrack: String
    private lateinit var track: Track

    private val playlistAdapter = PlayListAdapter {
        addTrackInPlaylist(it)
    }

    private var isPlaylistClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        track = getSerializable("track", Track::class.java)


        viewModel.observeStateLiveData().observe(this) {
            savedTimeTrack = it.progress
            binding.playButton.isEnabled = it.isPlayButtonEnabled
            binding.playButton.setImageResource(it.buttonResource)
            binding.trackTime.text = it.progress
        }

        viewModel.observeFavoriteState().observe(this) { isFavorite ->
            updateFavoriteButton(isFavorite)
            track.isFavorite = isFavorite
        }

        viewModel.checkIfFavorite(track)

        if (track.isFavorite) {
            binding.likeButton.setImageResource(R.drawable.button_was_liked)
        } else {
            binding.likeButton.setImageResource(R.drawable.button_liked_track)
        }

        viewModel.observePlaylistState().observe(this) {
            renderPlaylists(it)
        }
        binding.recyclerPlaylistsInPLayer.layoutManager = LinearLayoutManager(this)
        binding.recyclerPlaylistsInPLayer.adapter = playlistAdapter

        val bottomSheetContainer = binding.bottomSheetPlaylist
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.visibility = View.GONE
                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        viewModel.observeAddedToPlaylistState().observe(this) { addedToPlaylist ->
            when(addedToPlaylist) {
                is PlayListTrackState.Match -> renderToast(addedToPlaylist.playlistName, false)
                is PlayListTrackState.Added -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    renderToast(addedToPlaylist.playlistName, true)
                }
            }

        }


        binding.minAndSecTrack.text = track.trackTimeMillis
        binding.trackTime.text = track.trackTimeMillis
        artworkImageView = findViewById(R.id.artworkImageView)
        binding.nameTrackTextView.text = track.trackName
        binding.artistNameTextView.text = track.artistName
        binding.nameOfAlbum.text = track.collectionName ?: ""
        binding.yearRightSide.text = if (track.releaseDate != null) {
            formatYear.parse(track.releaseDate)?.let { formatYear.format(it) }
        } else {
            ""
        }
        binding.primaryGenreName.text = track.primaryGenreName ?: ""
        binding.countryRightSide.text = track.country ?: ""

        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform((RoundedCorners(convert.dpToPx(15f, this@AudioPlayer))))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(this.artworkImageView)


        viewModel.preparePlayer(previewUrl = track.previewUrl)

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.toolBar.setNavigationOnClickListener {
            this.finish()
        }

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        binding.addToPlayListButton.setOnClickListener {
            viewModel.getPlaylists()
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.halfExpandedRatio = 0.65F
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.newPlayListButton.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.playerFragmentContainer,
                AddPlayListFragment.newInstance(true)
            )
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.audioPlayerMainScreen.isVisible = false
            binding.overlay.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun renderToast(playlistName: String?, added: Boolean) {
        if (added) {

            Toast.makeText(
                this,
                getString(R.string.playlist_track_added, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            Toast.makeText(
                this,
                getString(R.string.playlist_track_exists, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun renderPlaylists(state: PlayListState) {
        when(state) {
            is PlayListState.Empty -> showEmpty()
            is PlayListState.Content -> showContent(state.playlists)
        }
    }

    private fun showEmpty() {
        binding.recyclerPlaylistsInPLayer.visibility = View.GONE
    }

    private fun showContent(playlists: List<PlayList>) {
        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()

        binding.recyclerPlaylistsInPLayer.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isPlaylistClickAllowed
        if (isPlaylistClickAllowed) {
            isPlaylistClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isPlaylistClickAllowed = true
            }
        }
        return current
    }

    private fun addTrackInPlaylist(playlist: PlayList) {
        if (clickDebounce()) {
            viewModel.addTrackInPlaylist(playlist, track)
        }
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.likeButton.setImageResource(R.drawable.button_was_liked)
        } else {
            binding.likeButton.setImageResource(R.drawable.button_liked_track)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(PLAY_TIME, binding.trackTime.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedTimeTrack = savedInstanceState.getCharSequence(PLAY_TIME).toString()

    }

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }

    companion object {
        const val PLAY_TIME = "PLAY_TIME"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}