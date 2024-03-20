package com.example.playlistmaker.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.DpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.trackTimeTextView)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artworkImageView)
    private val convert = DpToPx()

    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        trackTimeTextView.text = track.trackTimeMillis


        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .transform((RoundedCorners(convert.dpToPx(10f, itemView.context))))
            .placeholder(R.drawable.placeholder) // Заглушка, если нет интернета
            .error(R.drawable.placeholder)
            .into(artworkImageView)
    }

}