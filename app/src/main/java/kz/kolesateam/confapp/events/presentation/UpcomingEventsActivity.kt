package kz.kolesateam.confapp.events.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem
import kz.kolesateam.confapp.events.presentation.view.BranchAdapter
import kz.kolesateam.confapp.events.presentation.view.BranchClickListener
import kz.kolesateam.confapp.events.presentation.view.EventClickListener
import kz.kolesateam.confapp.events.presentation.view.FavoriteClickListener
import kz.kolesateam.confapp.events_details.presentation.EventDetailsActivity
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.favorite_events.presentation.FavoritesEventsActivity
import kz.kolesateam.confapp.models.ProgressState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BRANCH_ID = "id"
const val BRANCH_TITLE = "title"

class UpcomingEventsActivity : AppCompatActivity(), BranchClickListener{

    private val upcomingEventsViewModel: UpcomingEventsViewModel by viewModel()
    private val favoriteEventActionObservable: FavoriteEventActionObservable by inject()

    private val branchAdapter: BranchAdapter = BranchAdapter(
            branchClickListener = this@UpcomingEventsActivity,
            favoriteClickListener = getFavoriteClickListener(),
            eventClickListener = getEventClickListener(),
            favoriteEventActionObservable = favoriteEventActionObservable
    )

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_events)
        initViews()
        observeUpcomingEventsViewModel()
        upcomingEventsViewModel.onStart()
    }

    override fun onBranchClick(branchId: Int, branchTitle: String) {

        val intent = Intent(this,AllEventsActivity::class.java)
        intent.putExtra(BRANCH_ID, branchId)
        intent.putExtra(BRANCH_TITLE, branchTitle)
        startActivity(intent)
    }

    private fun initViews() {
        val favoritesButton: Button = findViewById(R.id.activity_upcoming_events_favorites)
        favoritesButton.setOnClickListener {
            startActivity(Intent(this,FavoritesEventsActivity::class.java))
        }
        progressBar = findViewById(R.id.activity_upcoming_events_progress_bar)
        recyclerView = findViewById(R.id.upcoming_events_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = branchAdapter
    }

    private fun observeUpcomingEventsViewModel() {
        upcomingEventsViewModel.getProgressLiveData().observe(this, ::handleProgressbarState)
        upcomingEventsViewModel.getUpcomingEventsLiveData().observe(this, ::showResult)
        upcomingEventsViewModel.getErrorLiveData().observe(this, ::showError)
    }

    private fun showResult(branchApiDataList: List<UpcomingEventListItem>) {
        branchAdapter.setList(branchApiDataList)
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
        return object : FavoriteClickListener{
            override fun onClick(eventData: EventApiData){
                upcomingEventsViewModel.onFavoriteClick(eventData)
            }
        }
    }

    private fun getEventClickListener(): EventClickListener {
        return object: EventClickListener{
            override fun onClick(id: Int) {
                val intent = Intent(
                    this@UpcomingEventsActivity,
                    EventDetailsActivity::class.java
                )
                intent.putExtra(PUSH_NOTIFICATION_EVENT_ID, id)
                startActivity(intent)
            }
        }
    }
}