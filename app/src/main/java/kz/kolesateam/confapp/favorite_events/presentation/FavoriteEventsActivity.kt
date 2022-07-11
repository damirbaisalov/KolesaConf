package kz.kolesateam.confapp.favorite_events.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.presentation.PUSH_NOTIFICATION_EVENT_ID
import kz.kolesateam.confapp.events.presentation.UpcomingEventsActivity
import kz.kolesateam.confapp.events.presentation.view.EventClickListener
import kz.kolesateam.confapp.events_details.presentation.EventDetailsActivity
import kz.kolesateam.confapp.favorite_events.presentation.view.FavoriteEventsAdapter
import kz.kolesateam.confapp.models.ProgressState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesEventsActivity : AppCompatActivity() {

    private val favoriteEventsViewModel: FavoriteEventsViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var astronautAndText: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var backToPreviousActivityButton: Button
    private val favoriteEventsAdapter: FavoriteEventsAdapter = FavoriteEventsAdapter(
        eventClickListener = getEventClickListener()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites_events)

        initViews()
        observeFavoriteEventsViewModel()
        favoriteEventsViewModel.onStart()
    }

    private fun initViews() {
        astronautAndText = findViewById(R.id.activity_favorite_events_astronaut_and_text_layout)
        progressBar = findViewById(R.id.activity_favorite_events_progress_bar)
        recyclerView = findViewById(R.id.activity_favorite_events_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = favoriteEventsAdapter

        backToPreviousActivityButton = findViewById(R.id.activity_favorites_events_back)
        backToPreviousActivityButton.setOnClickListener {
            navigateToUpcomingEventsActivity()
        }
    }

    private fun observeFavoriteEventsViewModel() {
        favoriteEventsViewModel.getProgressLiveData().observe(this, ::handleProgressbarState)
        favoriteEventsViewModel.getFavoriteEventsLiveData().observe(this, ::showResult)
        favoriteEventsViewModel.getErrorLiveData().observe(this, ::showError)
    }

    private fun showResult(favoriteEventsList: List<EventApiData>) {
        if (favoriteEventsList.isEmpty()) {
            recyclerView.visibility = View.INVISIBLE
            astronautAndText.visibility = View.VISIBLE
        } else {
            astronautAndText.visibility = View.INVISIBLE
            recyclerView.visibility = View.VISIBLE
        favoriteEventsAdapter.setList(favoriteEventsList)
        }
    }

    private fun showError(errorMessage: String){
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleProgressbarState(
        progressState: ProgressState
    ) {
        progressBar.isVisible = progressState is ProgressState.Loading
    }

    private fun getEventClickListener(): EventClickListener {
        return object: EventClickListener {
            override fun onClick(id: Int) {
                val intent = Intent(
                    this@FavoritesEventsActivity,
                    EventDetailsActivity::class.java
                )
                intent.putExtra(PUSH_NOTIFICATION_EVENT_ID, id)
                startActivity(intent)
            }
        }
    }

    private fun navigateToUpcomingEventsActivity() {
        val intent = Intent(this,UpcomingEventsActivity::class.java)
        startActivity(intent)
        finish()
    }
}