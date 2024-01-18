package com.example.playlistmaker
import SearchHistory
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val trackList: MutableList<Track>, private val historyManager: SearchHistory) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener{
            val clickedTrack = trackList[position]
            historyManager.saveTrackToHistory(clickedTrack)
            val intent = Intent(holder.itemView.context, AudioPlayer::class.java)
            intent.putExtra("track", clickedTrack)
            holder.itemView.context.startActivities(arrayOf(intent))

        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    fun setTrackList(trackList: List<Track>) {
        this.trackList.clear()
        this.trackList.addAll(trackList)
        notifyDataSetChanged()
        Log.d("TrackAdapter", "Updated trackList in adapter: $trackList")
    }


}