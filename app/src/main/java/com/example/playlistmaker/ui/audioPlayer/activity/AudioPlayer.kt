package com.example.playlistmaker.ui.audioPlayer.activity


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.DpToPx
import com.example.playlistmaker.ui.audioPlayer.view_model_audio_player.AudioPlayerViewModel
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayer : AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by viewModel()
    private lateinit var artworkImageView: ImageView
    private lateinit var binding: ActivityAudioPlayerBinding
    private val formatTime by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val formatYear by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }
    private val convert = DpToPx()
    private lateinit var savedTimeTrack: String
    private lateinit var track: Track

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
            if (isFavorite ) {
                binding.likeButton.setImageResource(R.drawable.button_was_liked)
            } else {
                binding.likeButton.setImageResource(R.drawable.button_liked_track)
            }
            track.isFavorite = !isFavorite
        }

        if (track.isFavorite)binding.likeButton.setImageResource(R.drawable.button_was_liked)


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
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
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
    }
}