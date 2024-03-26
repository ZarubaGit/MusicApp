package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.example.playlistmaker.ui.audioPlayer.activity.AudioPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.searchViewModel.SearchViewModel
import com.example.playlistmaker.databinding.ActivitySearchNewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {


    private var savedText: String? = null
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchNewBinding
    private lateinit var previousRequest: String
    private val simpleTextWatcher: TextWatcher? = null
    private val viewModel: SearchViewModel by viewModel()//внедрение зависимостей с помощью DI и Koin
    private lateinit var searchResultsAdapter: TrackAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchNewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeHistoryState().observe(this ) {
            showSearchHistory(it)
        }

        trackAdapter = TrackAdapter(setAdapterListener())
        searchResultsAdapter = TrackAdapter(setAdapterListener())

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
            hideSoftKeyboard()
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
                }
                previousRequest = s?.toString() ?: ""
                viewModel.searchDebounce(changedText = previousRequest)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        simpleTextWatcher.let { binding.inputEditText.addTextChangedListener(it) }



        // Обработка нажатия на поле ввода для отображения клавиатуры
        binding.inputEditText.setOnClickListener {
            showSoftKeyboard()
        }

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
        // Обработка нажатия на кнопку "Назад"
        binding.backInMain.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    private fun clearContent() {
        trackAdapter.trackList.clear()
        trackAdapter.notifyDataSetChanged()
    }


    // Метод для отображения клавиатуры
    private fun showSoftKeyboard() {
        binding.inputEditText.requestFocus()
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.showSoftInput(binding.inputEditText, InputMethodManager.SHOW_IMPLICIT)
    }
    //метод для скрытия клавиатуры
    private fun hideSoftKeyboard() {
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
    }

    //метод для сохранения текста при пересоздании активити
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, binding.inputEditText.text.toString())
    }
    //метод для сохранения текста при пересоздании активити
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(PRODUCT_AMOUNT, "")
        binding.inputEditText.setText(savedText)
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setAdapterListener(): TrackClickListener {
        return object : TrackClickListener {
            override fun onTrackClicked(track: Track) {
                // добавляем в историю поиска
                viewModel.onClick(track)

                // открываем аудиоплеер
                val displayIntent = Intent(this@SearchActivity, AudioPlayer::class.java)
                displayIntent.putExtra("track", TrackMapper.map(track))
                startActivity(displayIntent)
            }
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



    //константы
    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }
}