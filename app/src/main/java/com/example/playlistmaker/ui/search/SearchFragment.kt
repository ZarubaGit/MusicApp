package com.example.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchNewBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioPlayer.activity.AudioPlayer
import com.example.playlistmaker.ui.search.searchViewModel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var isClickAllowed = true
    private lateinit var binding: FragmentSearchNewBinding
    private lateinit var previousRequest: String
    private val simpleTextWatcher: TextWatcher? = null
    private val viewModel: SearchViewModel by viewModel()
    private val searchResultsAdapter = TrackAdapter(object : TrackAdapter.Listener {
        override fun onTrackClicked(track: Track) {
            enterToPlayer(track)
        }
    })
    private val trackAdapter = TrackAdapter(object : TrackAdapter.Listener {
        override fun onTrackClicked(track: Track) {
            enterToPlayer(track)
        }
    })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }


        viewModel.observeHistoryState().observe(viewLifecycleOwner) {
            showSearchHistory(it)
        }



        binding.recyclerTracks.adapter = trackAdapter
        binding.recyclerSearch.adapter = searchResultsAdapter


        //обработка нажатия по очистке истории поиска
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            searchResultsAdapter.trackList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            binding.searchPrefs.visibility = View.GONE
        }


        // Установка обработчика для кнопки "Очистить поисковый запрос"
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            binding.clearIcon.visibility = View.GONE
            binding.inputEditText.clearFocus()
            clearContent()
        }

        // Установка TextWatcher для поля ввода
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.recyclerTracks.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.placeholderMessage.visibility = View.GONE
                    binding.refreshButton.visibility = View.GONE
                    binding.imageHolder.visibility = View.GONE
                    binding.searchPrefs.visibility = View.GONE
                    binding.clearIcon.visibility = View.VISIBLE
                } else {
                    binding.clearIcon.visibility = View.GONE
                }
                previousRequest = s?.toString() ?: ""
                viewModel.searchDebounce(changedText = previousRequest)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        simpleTextWatcher.let { binding.inputEditText.addTextChangedListener(it) }

        //кнопка обновить запрос при отсутствии соединения
        binding.refreshButton.setOnClickListener {
            binding.inputEditText.setText(previousRequest)
            viewModel.searchDebounce(previousRequest)
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {
                    previousRequest = binding.inputEditText.text.toString()
                    viewModel.searchDebounce(binding.inputEditText.text.toString())

                }
            }
            false
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.getSearchHistory()
            binding.searchPrefs.visibility =
                if (hasFocus && binding.inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleTextWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    private fun clearContent() {
        trackAdapter.trackList.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun enterToPlayer(track: Track) {
        if (clickDebounce()) {
            viewModel.onClick(track)
            // открываем аудиоплеер
            val displayIntent = Intent(requireContext(), AudioPlayer::class.java)
            displayIntent.putExtra(TRACK, TrackMapper.map(track))
            startActivity(displayIntent)
        }
    }



    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Loading ->  showLoading()
            is TrackState.Error -> showError()
            is TrackState.Empty -> showEmpty()
            is TrackState.Content -> showContent(state.tracks)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.imageHolder.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE
        binding.recyclerTracks.visibility = View.GONE
    }

    private fun showError() {
        binding.imageHolder.setImageResource(R.drawable.network_error)
        binding.imageHolder.visibility = View.VISIBLE
        binding.placeholderMessage.setText(R.string.trouble_with_network)
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.imageHolder.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE
        binding.recyclerTracks.visibility = View.GONE
        binding.refreshButton.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.imageHolder.setImageResource(R.drawable.nothing_to_search)
        binding.placeholderMessage.setText(R.string.nothing_find)
        binding.imageHolder.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.recyclerTracks.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE

    }

    private fun showContent(newTracksList: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(newTracksList)
        trackAdapter.notifyDataSetChanged()
        binding.recyclerTracks.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        binding.imageHolder.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE
    }

    private fun showSearchHistory(track: ArrayList<Track> ) {
        if (track.isEmpty()) {
            binding.searchPrefs.visibility = View.GONE
        }
        else {
            searchResultsAdapter.trackList = track
            searchResultsAdapter.notifyDataSetChanged()
            binding.recyclerSearch.visibility = View.VISIBLE
            binding.recyclerTracks.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.placeholderMessage.visibility = View.GONE
            binding.imageHolder.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
            binding.searchPrefs.visibility = View.VISIBLE
        }


    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    //константы
    companion object {
        private const val DEBOUNCE_DELAY = 500L
        const val TRACK = "track"
    }
}

