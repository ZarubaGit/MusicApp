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

class AudioPlayer : AppCompatActivity(), DpToPx {

    private lateinit var backButton: ImageView
    private lateinit var artworkImageView: ImageView
    private lateinit var nameTrackTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var trackTime: TextView
    private lateinit var minAndSecTrack: TextView
    private lateinit var nameOfAlbum: TextView
    private lateinit var yearRightSide: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var countryRightSide: TextView
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playButton: ImageView
    private lateinit var handler: Handler
    private var isPlaying: Boolean = false
    private lateinit var updateTimeRunnable: Runnable
    private lateinit var pauseButton: Button
    private val formatTime by lazy {SimpleDateFormat("mm:ss", Locale.getDefault())}
    private val formatYear by lazy {SimpleDateFormat("yyyy", Locale.getDefault())}
    private val playPauseUseCase = PlayPauseUseCase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        backButton = binding.backInSearch
        artworkImageView = binding.artworkImageView
        artworkImageView.setImageResource(R.drawable.placeholder)
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

        playButton.setOnClickListener {
            val playing = playPauseUseCase.playingPause()
            if(playing){
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
                handler.post(updateTimeRunnable)
            }
            updatePlayButtonImage()
        }

        backButton.setOnClickListener {
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

    fun dataAssignment(track: Track?) {
        if (track != null) {
            val formattedTime = formatTime
                .format(track.trackTimeMillis)
            val releaseYear = if (track.releaseDate != null) {
                formatYear.format(formatYear.parse(track.releaseDate))
            } else {
                ""
            }
            minAndSecTrack.text = formattedTime
            trackTime.text = formattedTime
            nameTrackTextView.text = track.trackName
            artistNameTextView.text = track.artistName
            nameOfAlbum.text = track.collectionName ?: ""
            yearRightSide.text = releaseYear ?: ""
            primaryGenreName.text = track.primaryGenreName ?: ""
            countryRightSide.text = track.country ?: ""
            biblGlide(track = track)
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
            updateTimeRunnable = object :Runnable{
                override fun run() {
                    if(mediaPlayer.isPlaying && mediaPlayer != null){
                        val currentPosition = mediaPlayer.currentPosition
                        trackTime.text = formatTime
                            .format(currentPosition)
                        handler.postDelayed(this, TIME_IS_SECOND)
                    }
                }
            }

        }
    }

    private fun biblGlide(track: Track?){
        Glide.with(this)
            .load(track?.getCoverArtwork())
            .centerCrop()
            .transform((RoundedCorners(dpToPx(15f, this@AudioPlayer))))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(artworkImageView)
    }

    override fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun updatePlayButtonImage(){
        if(playPauseUseCase.isPlaying){
            playButton.setImageResource(R.drawable.button_play)
        } else {
            playButton.setImageResource(R.drawable.button_pause)
        }
    }
    companion object {
        private const val TIME_IS_SECOND = 1000L
    }
}