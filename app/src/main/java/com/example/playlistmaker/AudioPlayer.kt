package com.example.playlistmaker


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        backButton = findViewById(R.id.backInSearch)
        artworkImageView = findViewById(R.id.artworkImageView)
        artworkImageView.setImageResource(R.drawable.placeholder)
        nameTrackTextView = findViewById(R.id.nameTrackTextView)
        artistNameTextView = findViewById(R.id.artistNameTextView)
        trackTime = findViewById(R.id.trackTime)
        minAndSecTrack = findViewById(R.id.minAndSecTrack)
        nameOfAlbum = findViewById(R.id.nameOfAlbum)
        yearRightSide = findViewById(R.id.yearRightSide)
        primaryGenreName = findViewById(R.id.primaryGenreName)
        countryRightSide = findViewById(R.id.countryRightSide)


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