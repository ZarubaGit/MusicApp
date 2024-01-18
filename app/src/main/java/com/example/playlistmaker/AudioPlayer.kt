package com.example.playlistmaker


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
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


        val track: Track? = intent.getSerializableExtra("track") as Track?
        dataAssignment(track)

        backButton.setOnClickListener{
            finish()
        }
    }
    fun dataAssignment(track: Track?){
        if(track != null){
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
            nameOfAlbum.text = track.collectionName?:""
            yearRightSide.text = releaseYear?:""
            primaryGenreName.text = track.primaryGenreName?:""
            countryRightSide.text = track.country?:""
            Glide.with(this)
                .load(track.getCoverArtwork())
                .centerCrop()
                .transform((RoundedCorners(dpToPx(15f, this@AudioPlayer))))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(artworkImageView)

        }
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}