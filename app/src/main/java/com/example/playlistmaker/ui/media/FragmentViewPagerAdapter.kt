package com.example.playlistmaker.ui.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle
import com.example.playlistmaker.ui.media.playList.PlaylistsFragment

class FragmentViewPagerAdapter(fragmetManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmetManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0) FavoriteSongsFragment.newInstance() else PlaylistsFragment.newInstance()
    }
}