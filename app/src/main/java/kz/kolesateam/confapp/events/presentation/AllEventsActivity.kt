package kz.kolesateam.confapp.events.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.threetenabp.AndroidThreeTen
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem
import kz.kolesateam.confapp.events.presentation.view.BranchEventsAdapter
import kz.kolesateam.confapp.events.presentation.view.EventClickListener
import kz.kolesateam.confapp.events.presentation.view.FavoriteClickListener
import kz.kolesateam.confapp.events_details.presentation.EventDetailsActivity
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.favorite_events.presentation.FavoritesEventsActivity
import kz.kolesateam.confapp.models.ProgressState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllEventsActivity : AppCompatActivity(){

    private val allEventsViewModel: AllEventsViewModel by viewModel()
    private val favoriteEventActionObservable: FavoriteEventActionObservable by inject()

    private val branchEventsAdapter: BranchEventsAdapter = BranchEventsAdapter(
        favoriteClickListener = getFavoriteClickListener(),
        eventClickListener = getEventClickListener(),
        favoriteEventActionObservable = favoriteEventActionObservable
    )

    private lateinit var branchTitle: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var backToPreviousActivityImageView: ImageView

    private var branchId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_events)
        AndroidThreeTen.init(this)

        branchId = intent.getIntExtra(BRANCH_ID, 0)
        branchTitle = intent.getStringExtra(BRANCH_TITLE).toString()

        initViews()
        observeAllEventsViewModel()
        allEventsViewModel.onStart(
            branchId = branchId,
            branchTitle = branchTitle
        )
    }

    private fun initViews() {
        val favoritesButton: Button = findViewById(R.id.activity_all_events_favorites)
        favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesEventsActivity::class.java))
        }
        backToPreviousActivityImageView = findViewById(R.id.back_to_previous_activity)
        backToPreviousActivityImageView.setOnClickListener {
            finish()
        }
        progressBar = findViewById(R.id.activity_all_events_progress_bar)
        recyclerView = findViewById(R.id.activity_all_events_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = branchEventsAdapter
    }

    private fun observeAllEventsViewModel() {
        allEventsViewModel.getProgressLiveData().observe(this, ::handleProgressbarState)
        allEventsViewModel.getAllEventsLiveData().observe(this, ::showResult)
        allEventsViewModel.getErrorLiveData().observe(this, ::showError)
    }

    private fun showResult(branchEventsDataList: List<UpcomingEventListItem>) {
        branchEventsAdapter.setList(branchEventsDataList)
    }

    private fun showError(errorMessage: String){
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleProgressbarState(
        progressState: ProgressState
    ) {
        progressBar.isVisible = progressState is ProgressState.Loading
    }

    private fun getFavoriteClickListener(): FavoriteClickListener {
        return object : FavoriteClickListener {
            override fun onClick(eventData: EventApiData) {
                allEventsViewModel.onFavoriteClick(eventData)
            }
        }
    }

    private fun getEventClickListener(): EventClickListener {
        return object: EventClickListener {
            override fun onClick(id: Int) {
                val intent = Intent(
                    this@AllEventsActivity,
                    EventDetailsActivity::class.java
                )
                intent.putExtra(PUSH_NOTIFICATION_EVENT_ID, id)
                startActivity(intent)
            }
        }
    }
}