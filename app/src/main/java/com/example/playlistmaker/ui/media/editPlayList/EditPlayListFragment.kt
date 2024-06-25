package com.example.playlistmaker.ui.media.editPlayList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.addPlayList.AddPlayListFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlayListFragment: AddPlayListFragment() {
    override val viewModel: EditPlaylistFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistID = requireArguments().getInt(PLAYLIST_ID, 0)
        if (playlistID > 0) {
            viewModel.getPlaylist(playlistID)
        }
        else {
            findNavController().popBackStack()
        }
        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            binding.inputEdittextName.setText(it.name)
            binding.inputEdittextDescription.setText(it.description)
            if (it.uri != null) {
                Glide.with(this)
                    .load(it.uri)
                    .placeholder(R.drawable.placeholder_playlist)
                    .transform(CenterCrop())
                    .into(binding.playlistImage)
            }
            tempPlayList = it
        }
        binding.newPlaylistHeader.text = getText(R.string.edit)
        binding.createButton.text = getText(R.string.save)

        binding.createButton.setOnClickListener {
            lifecycleScope.launch { viewModel.savePlaylist(tempPlayList) }
            findNavController().popBackStack()
        }

        binding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    companion object {
        private const val PLAYLIST_ID = "playlistID"
        fun createArgs(playlistId: Int?): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}