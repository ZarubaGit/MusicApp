package com.example.playlistmaker.ui.audioPlayer


import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.PlayPauseUseCase
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.DpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

    private var backButton: ImageView? = null
    private var artworkImageView: ImageView? = null
    private var nameTrackTextView: TextView? = null
    private var artistNameTextView: TextView? = null
    private var trackTime: TextView? = null
    private var minAndSecTrack: TextView? = null
    private var nameOfAlbum: TextView? = null
    private var yearRightSide: TextView? = null
    private var primaryGenreName: TextView? = null
    private var countryRightSide: TextView? = null
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var playButton: ImageView? = null
    private lateinit var handler: Handler
    private var isPlaying: Boolean = false
    private lateinit var updateTimeRunnable: Runnable
    private var pauseButton: Button? = null
    private val formatTime by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val formatYear by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }
    private val playPauseUseCase = PlayPauseUseCase()
    private val convert = DpToPx()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        backButton = binding.backInSearch
        artworkImageView = binding.artworkImageView
        artworkImageView?.setImageResource(R.drawable.placeholder)
        nameTrackTextView = binding.nameTrackTextView
        artistNameTextView = binding.artistNameTextView
        trackTime = binding.trackTime
        minAndSecTrack = binding.minAndSecTrack
        nameOfAlbum = binding.nameOfAlbum
        yearRightSide = binding.yearRightSide
        primaryGenreName = binding.primaryGenreName
        countryRightSide = binding.countryRightSide
        playButton = binding.playButton



        val track: Track? = intent.getSerializableExtra("track") as Track?
        dataAssignment(track)

        handler = Handler()

        playButton?.setOnClickListener {
            val playing = playPauseUseCase.playingPause()
            if(playing){
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
                handler.post(updateTimeRunnable)
            }
            updatePlayButtonImage()
        }

        backButton?.setOnClickListener {
            mediaPlayer.release()
            handler.removeCallbacks(updateTimeRunnable)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimeRunnable)

    }

    private fun dataAssignment(track: Track?) {
        if (track != null) {
            val formattedTime = formatTime
                .format(track.trackTimeMillis)
            val releaseYear = if (track.releaseDate != null) {
                formatYear.format(formatYear.parse(track.releaseDate))
            } else {
                ""
            }
            minAndSecTrack?.text = formattedTime
            trackTime?.text = formattedTime
            nameTrackTextView?.text = track.trackName
            artistNameTextView?.text = track.artistName
            nameOfAlbum?.text = track.collectionName ?: ""
            yearRightSide?.text = releaseYear ?: ""
            primaryGenreName?.text = track.primaryGenreName ?: ""
            countryRightSide?.text = track.country ?: ""
            biblGlide(track = track)
            mediaPlayerImpl(track)
            updateTime()
        }
    }

    private fun mediaPlayerImpl(track: Track){
        mediaPlayer = MediaPlayer().apply {
            val trackUrl = Uri.parse(track.previewUrl)
            setDataSource(this@AudioPlayer, trackUrl)
            prepareAsync()
            setOnPreparedListener {
                start()
                handler.post(updateTimeRunnable)
                this@AudioPlayer.isPlaying = true
                updatePlayButtonImage()
            }
            setOnCompletionListener {
                this@AudioPlayer.isPlaying = false
                updatePlayButtonImage()
                handler.removeCallbacks(updateTimeRunnable)
            }
        }
    }

    private fun updateTime(){
        updateTimeRunnable = object :Runnable{
            override fun run() {
                if(mediaPlayer.isPlaying && mediaPlayer != null){
                    val currentPosition = mediaPlayer.currentPosition
                    trackTime?.text = formatTime
                        .format(currentPosition)
                    handler.postDelayed(this, TIME_IS_SECOND)
                }
            }
        }
    }

    private fun biblGlide(track: Track?){
        Glide.with(this)
            .load(track?.getCoverArtwork())
            .centerCrop()
            .transform((RoundedCorners(convert.dpToPx(15f, this@AudioPlayer))))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(artworkImageView!!)
    }

    private fun updatePlayButtonImage(){
        if(playPauseUseCase.isPlaying){
            playButton?.setImageResource(R.drawable.button_play)
        } else {
            playButton?.setImageResource(R.drawable.button_pause)
        }
    }
    companion object {
        private const val TIME_IS_SECOND = 1000L
    }
}