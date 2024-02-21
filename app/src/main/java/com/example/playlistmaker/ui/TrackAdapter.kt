package com.example.playlistmaker.ui
import com.example.playlistmaker.data.dto.SearchHistory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.TrackClickListener
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(
    val trackList: MutableList<Track>,
    private val historyManager: SearchHistory,
    private var clickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            val clickedTrack = trackList[position]
            historyManager.saveTrackToHistory(clickedTrack)
            clickListener.onTrackClicked(clickedTrack)

        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    fun setTrackList(trackList: List<Track>) {
        this.trackList.clear()
        this.trackList.addAll(trackList)
        notifyDataSetChanged()
    }


}