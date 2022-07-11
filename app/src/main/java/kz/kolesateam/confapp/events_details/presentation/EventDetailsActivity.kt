package kz.kolesateam.confapp.events_details.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.presentation.PUSH_NOTIFICATION_EVENT_ID
import kz.kolesateam.confapp.events.presentation.view.FavoriteClickListener
import kz.kolesateam.confapp.events.presentation.view.TIME_FORMAT
import kz.kolesateam.confapp.events.presentation.view.TIME_STRING_END
import kz.kolesateam.confapp.events.presentation.view.TIME_STRING_START
import kz.kolesateam.confapp.models.ProgressState
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailsActivity : AppCompatActivity() , FavoriteClickListener{

    private val eventDetailsViewModel: EventDetailsViewModel by viewModel()

    private lateinit var speakerName: TextView
    private lateinit var speakerJob: TextView
    private lateinit var startEndTimeAndPlace: TextView
    private lateinit var evenTitle: TextView
    private lateinit var eventDescription: TextView
    private lateinit var speakerImage: ImageView
    private lateinit var isInvitedSpeaker: TextView
    private lateinit var favoriteButton: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var backToPreviousActivityImageView: ImageView

    private var eventId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val messageFromPushID: Int? = intent?.getIntExtra(PUSH_NOTIFICATION_EVENT_ID,0)
        messageFromPushID?.let {
            eventId = messageFromPushID
        }
        initView()
        observeEventDetailsViewModel()
        eventDetailsViewModel.onStart(id = eventId)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val messageFromPushID: Int? = intent?.getIntExtra(PUSH_NOTIFICATION_EVENT_ID,0)
        messageFromPushID?.let {
            eventId = messageFromPushID
        }
    }

    override fun onClick(eventData: EventApiData) {
        eventDetailsViewModel.onFavoriteClick(eventData)
    }

    private fun initView() {
        speakerName = findViewById(R.id.activity_event_details_speaker_name)
        speakerJob = findViewById(R.id.activity_event_details_job)
        startEndTimeAndPlace = findViewById(R.id.activity_event_details_time_start_end_place)
        evenTitle = findViewById(R.id.activity_event_details_title)
        eventDescription = findViewById(R.id.activity_event_details_content)
        speakerImage = findViewById(R.id.speaker_image)
        isInvitedSpeaker = findViewById(R.id.speaker_is_invited_text_view)
        favoriteButton = findViewById(R.id.activity_event_details_favorite_button)
        progressBar = findViewById(R.id.activity_event_details_progress_bar)

        backToPreviousActivityImageView = findViewById(R.id.activity_event_details_back_to_previous_activity)
        backToPreviousActivityImageView.setOnClickListener {
            finish()
        }
    }

    private fun observeEventDetailsViewModel() {
        eventDetailsViewModel.getProgressLiveData().observe(this, ::handleProgressbarState)
        eventDetailsViewModel.getEventDetailsLiveData().observe(this, ::showResult)
        eventDetailsViewModel.getErrorLiveData().observe(this, ::showError)
    }

    private fun showResult(eventData: EventApiData) {
        bindData(eventData = eventData)
    }

    private fun showError(errorMessage: String){
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleProgressbarState(
        progressState: ProgressState
    ) {
        progressBar.isVisible = progressState is ProgressState.Loading
    }

    private fun bindData(eventData: EventApiData) {

        favoriteButton.setOnClickListener {
            eventData.isFavorite = !eventData.isFavorite
            setImageResourceState(eventData.isFavorite, favoriteButton)
            onClick(eventData)
        }

        favoriteButton.setImageResource(getFavoriteImageResource(eventData.isFavorite))

        speakerName.text = eventData.speaker?.fullName
        speakerJob.text = eventData.speaker?.job

        val eventDateAndPlaceText = TIME_FORMAT.format(
            eventData.startTime?.substring(TIME_STRING_START, TIME_STRING_END),
            eventData.endTime?.substring(TIME_STRING_START, TIME_STRING_END),
            eventData.place
        )

        startEndTimeAndPlace.text = eventDateAndPlaceText
        evenTitle.text = eventData.title
        eventDescription.text = eventData.description

        if (!eventData.speaker?.isInvited!!) {
//            isInvitedSpeaker.text = "Не приглашен"
//            isInvitedSpeaker.isEnabled = false

            isInvitedSpeaker.visibility = View.INVISIBLE
        } else {
            isInvitedSpeaker.text = "Приглашенный спикер"
            isInvitedSpeaker.isEnabled = true
        }

        Glide
            .with(this@EventDetailsActivity)
            .load(eventData.speaker.photoUrl)
            .into(speakerImage)
    }

    private fun setImageResourceState(
        isFavorite: Boolean,
        favoriteImageView: ImageView
    ) {
        when (isFavorite) {
            true -> favoriteImageView.setImageResource(R.drawable.activity_event_details_ic_like_fill)
            false -> favoriteImageView.setImageResource(R.drawable.activity_event_details_ic_like_border)
        }
    }

    private fun getFavoriteImageResource(
        isFavorite: Boolean,
    ): Int = when (isFavorite) {
        true -> R.drawable.activity_event_details_ic_like_fill
        else -> R.drawable.activity_event_details_ic_like_border
    }
}