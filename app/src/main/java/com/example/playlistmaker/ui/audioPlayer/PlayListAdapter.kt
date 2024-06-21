package com.example.playlistmaker.ui.audioPlayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList

class PlayListAdapter(private val listener: Listener) : RecyclerView.Adapter<PlayListViewHolder> () {

    var playlists = mutableListOf<PlayList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.add_to_playlist_item, parent, false)
        return PlayListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)

        holder.itemView.setOnClickListener {
            listener.onClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun interface Listener {
        fun onClick(playlist: PlayList)
    }
}