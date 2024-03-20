package com.example.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.data.dto.SearchHistory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.TrackClickListener
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(

    private var clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList = ArrayList<Track>()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            val clickedTrack = trackList[position]
            if(clickDebounce()) {
                clickListener.onTrackClicked(clickedTrack)
            }

        }
    }
    private fun clickDebounce() : Boolean {
        val  current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    fun setTrackList(trackList: List<Track>) {
        this.trackList.clear()
        this.trackList.addAll(trackList)
        notifyDataSetChanged()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}