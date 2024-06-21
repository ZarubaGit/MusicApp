package com.example.playlistmaker.ui.audioPlayer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList

class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlistNameTextView)
    private val playlistTrackCounter: TextView = itemView.findViewById(R.id.countsTrack)
    private val cover: ImageView = itemView.findViewById(R.id.placeholderPlayList)

    fun bind(item: PlayList) {
        playlistName.text = item.name
        cover.contentDescription = item.name
        playlistTrackCounter.text = item.tracksCounter.let {
            itemView.resources.getQuantityString(
                R.plurals.track_counter,
                it,
                item.tracksCounter
            )
        }

        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(itemView.context.resources.getDimensionPixelOffset(R.dimen.dimen8dp)))
            .into(itemView.findViewById(R.id.placeholderPlayList))
    }
}