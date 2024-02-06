package com.example.playlistmaker

import SearchHistory
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackClickListener {

    private val urlApi = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(urlApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunseService = retrofit.create(ApiSong::class.java)
    private val trackList = mutableListOf<Track>()
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var linearLayout: LinearLayout
    private lateinit var savedText: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var adapter: TrackAdapter
    private lateinit var imageHolder: ImageView
    private lateinit var textHolderMessage: TextView
    private lateinit var udpateButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var clearHistoryButton: Button
    private var lastSearchQuery: String? = null
    private lateinit var historyContainer: LinearLayout
    private lateinit var binding: ActivitySearchBinding
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { performSearch(inputEditText.text.toString()) }

    private lateinit var progressBar: ProgressBar
    private var isClickAllowed: Boolean = true
    private var clickedTrack: Track? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        inputEditText = binding.inputEditText
        clearButton = binding.clearIcon
        linearLayout = binding.container
        backButton = binding.backInMain
        recyclerView = binding.recyclerView
        textHolderMessage = binding.placeholderMessage
        udpateButton = binding.refreshButton
        imageHolder = binding.imageHolder
        searchHistory = SearchHistory(this)
        clearHistoryButton = binding.clearHistoryButton
        historyContainer = binding.historyContainer
        progressBar = binding.progressBar


        clearHistoryButton.setOnClickListener {
            clearSearchHistory()
        }

        setupRecyclerView()

        // Переход с элемента поиска на экран Аудиоплеера
        recyclerView.setOnClickListener { view ->
            if (trackDebounce()) {
                val position = recyclerView.getChildAdapterPosition(view)
                if (position != RecyclerView.NO_POSITION) {
                    val localClickedTrack = adapter.trackList[position]
                    clickedTrack = localClickedTrack
                    handleClick(localClickedTrack)
                }
            }
        }

        // Установка обработчика для кнопки "Очистить поисковый запрос"
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideSoftKeyboard()
            clearButton.visibility = View.GONE
            textHolderMessage.visibility = View.GONE
            imageHolder.visibility = View.GONE
            adapter.setTrackList(trackList = searchHistory.getSearchHistory())
            adapter.notifyDataSetChanged()
        }

        // Установка TextWatcher для поля ввода
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                updateUI()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)


        // Обработка нажатия на поле ввода для отображения клавиатуры
        inputEditText.setOnClickListener {
//            inputEditText.text.clear()
            showSoftKeyboard()

            // Показ клавиатуры
        }

        udpateButton.setOnClickListener {
            val query = lastSearchQuery
            if (query != null) {
                performSearch(query)
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            val input = inputEditText.text.toString()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch(input)
                lastSearchQuery = input
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideSearchUI()
            }
        }
        // Обработка нажатия на кнопку "Назад"
        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    private fun clearSearchHistory() {
        searchHistory.clearSearchHistory()
        adapter.setTrackList(emptyList())
        updateUI()
    }

    private fun updateUI() {
        val history = searchHistory.getSearchHistory()

        if (history.isNotEmpty()) {
            historyContainer.visibility = View.VISIBLE
            clearHistoryButton.visibility = View.VISIBLE
            udpateButton.visibility = View.GONE
            adapter.setTrackList(history)  // Обновление данных в адаптере
        } else {
            clearHistoryButton.visibility = View.GONE
            historyContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    private fun hideSearchUI() {
        adapter.setTrackList(emptyList())
        historyContainer.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE

    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun handleClick(clickedTrack: Track) {
        val intent = Intent(this, AudioPlayer::class.java)
        intent.putExtra(TRACK_KEY, clickedTrack)
        startActivity(intent)
    }

    private fun trackDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({
                isClickAllowed = true
            }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            return
        }
        progressBar.visibility = View.VISIBLE
        hideSearchUI()
        iTunseService.search(query).enqueue(object : Callback<SongResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                val bodyResponse = response.body()?.results
                if (response.isSuccessful) {
                    trackList.clear()
                    progressBar.visibility = View.GONE
                    if (bodyResponse?.isNotEmpty() == true) {
                        trackList.addAll(bodyResponse)
                        adapter.setTrackList(trackList)
                        adapter.notifyDataSetChanged()
                        recyclerView.visibility = View.VISIBLE
                    }
                    if (trackList.isEmpty()) {
                        imageHolder.visibility = View.VISIBLE
                        imageHolder.setImageResource(R.drawable.nothing_to_search)
                        showMessage(getString(R.string.nothing_find), "")
                    } else {
                        showMessage("", "")
                    }
                    udpateButton.visibility = View.GONE
                } else {
                    lastSearchQuery = query
                    imageHolder.visibility = View.VISIBLE
                    showMessage(
                        getString(R.string.trouble_with_network),
                        response.code().toString()
                    )
                    updateUI()
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                lastSearchQuery = query
                imageHolder.visibility = View.VISIBLE
                imageHolder.setImageResource(R.drawable.network_error)
                showMessage(getString(R.string.trouble_with_network), t.message.toString())
                udpateButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                hideSearchUI()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            textHolderMessage.visibility = View.VISIBLE
            trackList.clear()
            adapter.notifyDataSetChanged()
            textHolderMessage.text = text
        } else {
            textHolderMessage.visibility = View.GONE
        }
    }


    override fun onTrackClicked(track: Track) {
        if (trackDebounce()) {
            handleClick(track)
        }
    }
    private fun setupRecyclerView() {
        val sh = searchHistory.getSearchHistory()
        adapter = TrackAdapter(sh, searchHistory, this)
        searchHistory.getSearchHistory()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        updateUI()
    }

    // Метод для отображения клавиатуры
    private fun showSoftKeyboard() {
        inputEditText.requestFocus()
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftKeyboard() {
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }

    // Метод для определения видимости кнопки "Очистить поисковый запрос"
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(PRODUCT_AMOUNT, "")
        inputEditText.setText(savedText)
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
        private const val CLICK_DEBOUNCE_DELAY = 500L
        const val TRACK_KEY = "track"
    }
}