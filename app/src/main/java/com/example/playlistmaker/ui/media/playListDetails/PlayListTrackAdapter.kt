package com.example.playlistmaker.ui.media.playListDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class PlayListTrackAdapter(private val listener: Listener) : RecyclerView.Adapter<PlaylistTrackViewHolder> () {
    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return PlaylistTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            listener.onShortClick(track = track)
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongClick(track.trackId)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface Listener {
        fun onShortClick(track: Track)
        fun onLongClick(trackId: Int): Boolean
    }

}