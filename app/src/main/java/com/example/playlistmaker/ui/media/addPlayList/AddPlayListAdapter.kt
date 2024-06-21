package com.example.playlistmaker.ui.media.addPlayList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList

class AddPlayListAdapter : RecyclerView.Adapter<AddPlayListViewHolder> () {

    var playlists = mutableListOf<PlayList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPlayListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return AddPlayListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddPlayListViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}