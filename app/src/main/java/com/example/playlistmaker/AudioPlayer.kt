package com.example.playlistmaker


import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {

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
            if(isPlaying){
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
                handler.post(updateTimeRunnable)
            }
            isPlaying = !isPlaying
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
            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(track.trackTimeMillis)
            val formattedData = SimpleDateFormat("yyyy", Locale.getDefault())
            val releaseYear = if (track.releaseDate != null) {
                formattedData.format(formattedData.parse(track.releaseDate))
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
            Glide.with(this)
                .load(track.getCoverArtwork())
                .centerCrop()
                .transform((RoundedCorners(dpToPx(15f, this@AudioPlayer))))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(artworkImageView)
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
                        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format(currentPosition)
                        handler.postDelayed(this, TIME_IS_SECOND)
                    }
                }
            }

        }
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun updatePlayButtonImage(){
        if(isPlaying){
            playButton.setImageResource(R.drawable.button_pause)
        } else {
            playButton.setImageResource(R.drawable.button_play)
        }
    }
    companion object {
        private const val TIME_IS_SECOND = 1000L
    }
}