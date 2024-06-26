package com.example.playlistmaker.ui.media.playListDetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.editPlayList.EditPlayListFragment
import com.example.playlistmaker.ui.media.playList.PlayListTrackState
import com.example.playlistmaker.ui.media.playListDetails.view.PlaylistDetailsFragmentViewModel
import com.example.playlistmaker.util.Changer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistDetailsBinding
    private var numberOfTracksString: String = ""
    private var playlistTracks: List<Track> = listOf()
    private val viewModel: PlaylistDetailsFragmentViewModel by viewModel()

    private var isClickAllowed = true
    private lateinit var playlistTrackAdapter: PlayListTrackAdapter
    private lateinit var playlistTrackAdapterListener: PlayListTrackAdapter.Listener

    private lateinit var bottomSheetMenu: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetTracks: BottomSheetBehavior<LinearLayout>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistID = requireArguments().getInt(PLAYLIST_ID, 0)
        if (playlistID > 0) {
            viewModel.getPlaylist(playlistID)
            viewModel.getTracksForPlaylist(playlistID)
        } else {
            findNavController().popBackStack()
        }
        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            initPlaylist(it)
        }
        viewModel.observeTracks().observe(viewLifecycleOwner) {
            initPlaylistTracks(it)
        }
        playlistTrackAdapterListener = object : PlayListTrackAdapter.Listener {
            override fun onShortClick(track: Track) {
                if (clickDebounce()) {
                    val bundle = Bundle()
                    bundle.putParcelable(TRACK_INFO, track)
                    findNavController().navigate(R.id.actionGlobalPlayer,
                        bundle)
                }
            }

            override fun onLongClick(trackId: Int): Boolean {
                showDeleteTrackDialog(trackId)
                return true
            }

        }
        playlistTrackAdapter = PlayListTrackAdapter(playlistTrackAdapterListener)
        binding.recyclerPlaylistTracks.adapter = playlistTrackAdapter

        bottomSheetMenu = BottomSheetBehavior.from(binding.BottomSheetPlaylistMenu)
        bottomSheetTracks = BottomSheetBehavior.from(binding.bottomSheetPlaylistTracks)
        bottomSheetMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlayPl.visibility = View.GONE
                    }
                    else -> {
                        binding.overlayPl.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        bottomSheetTracks.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun initPlaylist(playlist: PlayList) {
        binding.playlistName.text = playlist.name
        binding.playlistNameMenu.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.playlistDescriptionMenu.text = playlist.description
        numberOfTracksString = resources.getQuantityString(R.plurals.track_counter, playlist.tracks.size, playlist.tracks.size)
        val numberOfMinutesString: String = resources.getQuantityString(
            R.plurals.minutes_counter,
            Changer.convertMillisToMinutes(playlist.trackTimerMillis).toInt(),
            Changer.convertMillisToMinutes(playlist.trackTimerMillis).toInt()
        )

        val builder = StringBuilder()
        builder.append(numberOfMinutesString)
        builder.append(" \u2022 ")
        builder.append(numberOfTracksString)
        binding.playlistStats.text = builder.toString()

        Glide.with(this)
            .load(playlist.uri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop())
            .into(binding.playlistDetailsCover)

        Glide.with(this)
            .load(playlist.uri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop())
            .into(binding.playlistDetailsCoverMenu)

        binding.playlistDetailsCover.contentDescription =
            "${playlist.name}  :  + ${playlist.description}"

        binding.buttonPlaylistShare.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }
        binding.buttonPlaylistMenu.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.buttonPlaylistShare2.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }
        binding.buttonPlaylistDelete.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            showDeletePlaylistDialog()
        }
        binding.buttonPlaylistEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistDetailsFragment_to_editPlayListFragment2,
                EditPlayListFragment.createArgs(playlist.id)
            )
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

        binding.buttonPlaylistShare.post {
            bottomSheetTracks.apply {
                peekHeight =
                    binding.root.height - binding.buttonPlaylistShare.bottom - resources.getDimensionPixelSize(
                        R.dimen.dimen24dp
                    )
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun initPlaylistTracks(state: PlayListTrackState) {
        when (state) {
            is PlayListTrackState.Empty -> {
                showEmpty()
                playlistTracks = listOf()
            }

            is PlayListTrackState.Content -> {
                showContent(state.tracks)
                playlistTracks = state.tracks
            }

            else -> {}
        }
    }

    private fun showContent(tracks: List<Track>) {
        binding.playlistEmpty.visibility = View.GONE
        playlistTrackAdapter.tracks.clear()
        playlistTrackAdapter.tracks.addAll(tracks)
        playlistTrackAdapter.notifyDataSetChanged()

        binding.recyclerPlaylistTracks.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.recyclerPlaylistTracks.visibility = View.GONE
        binding.playlistEmpty.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun showDeleteTrackDialog(trackId: Int) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.track_delete)
            .setMessage(R.string.track_delete_from_playlist_message)
            .setNegativeButton(R.string.no) { _, _ ->
            }
            .setPositiveButton(R.string.yes) { _, _ ->
                val playlistId = requireArguments().getInt(PLAYLIST_ID, 0)
                if (playlistId > 0)
                    viewModel.deleteTrackFromPlaylist(playlistId, trackId)
            }.show()
    }

    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.playlist_delete)
            .setMessage(R.string.playlist_delete_message)
            .setNegativeButton(R.string.no) { _, _ ->
            }
            .setPositiveButton(R.string.yes) { _, _ ->
                val playlistId = requireArguments().getInt(PLAYLIST_ID, 0)
                if (playlistId > 0) {
                    viewModel.deletePlaylist(playlistId)
                    findNavController().popBackStack()
                }
            }.show()
    }

    private fun sharePlaylist() {
        if (playlistTracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_no_tracks_to_share),
                Toast.LENGTH_LONG
            ).show()
        } else {
            var sharePlaylistMessage =
                "${binding.playlistName.text}\n${binding.playlistDescription.text}\n${numberOfTracksString}\n\n"

            for (i in playlistTracks.indices) {
                sharePlaylistMessage += "${i + 1}. ${playlistTracks[i].artistName} - ${playlistTracks[i].trackName} (${
                    playlistTracks[i].trackTimeMillis?.let {
                        Changer.convertMillisToMinutesAndSeconds(
                            it
                        )
                    }
                })\n"
            }

            viewModel.shareApp(sharePlaylistMessage)
        }
    }

    override fun onResume() {
        super.onResume()
        bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
        isClickAllowed = true
    }

    companion object {
        private const val PLAYLIST_ID = "playlistID"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val TRACK_INFO = "track"
        fun createArgs(playlistId: Int?): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}
