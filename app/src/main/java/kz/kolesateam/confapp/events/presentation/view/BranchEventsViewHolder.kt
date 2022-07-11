package kz.kolesateam.confapp.events.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.favorite_events.domain.model.FavoriteActionEvent
import org.threeten.bp.ZonedDateTime
import java.util.*

class BranchEventsViewHolder(
    itemView: View,
    private val favoriteClickListener: FavoriteClickListener,
    private val eventClickListener: EventClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable
) : RecyclerView.ViewHolder(itemView) {

    private val favoriteObserver: Observer = object: Observer {
        override fun update(o: Observable?, favoriteEventActionObject: Any?) {
            val favoriteActionEvent = (favoriteEventActionObject as? FavoriteActionEvent) ?: return

            if (eventData.id == favoriteActionEvent.eventId) {
                favoriteImageView.setImageResource(
                    getFavoriteImageResource(favoriteActionEvent.isFavorite)
                )
            }
        }
    }

    private val branchEvent: View = itemView.findViewById(R.id.branch_event)
    private val eventState: TextView = itemView.findViewById(R.id.event_state)

    private val eventDateAndPlaceTextView: TextView = itemView.findViewById(R.id.time_start_end_place)
    private val speakerName: TextView = itemView.findViewById(R.id.speaker_name)
    private val speakerJob: TextView = itemView.findViewById(R.id.job)
    private val eventTitle: TextView = itemView.findViewById(R.id.event_title)
    private val favoriteImageView: ImageView = itemView.findViewById(R.id.ic_favorite)
    private lateinit var eventData: EventApiData

    init {
       eventState.visibility = View.INVISIBLE
    }

    fun onBind(eventApiData: EventApiData) {

        eventData = (eventApiData as? EventApiData) ?: return

        if (eventApiData.isFavorite)
            favoriteImageView.setImageResource(R.drawable.ic_favorite_fill)

        favoriteImageView.setOnClickListener {
            eventApiData.isFavorite = !eventApiData.isFavorite
            setImageResourceState(eventApiData.isFavorite, favoriteImageView)
            favoriteClickListener.onClick(eventApiData)
        }

        branchEvent.setOnClickListener {
            eventClickListener.onClick(eventApiData.id!!)
        }

        val eventEndTimeParsed = ZonedDateTime.parse(eventApiData.endTime)
        if (isEventFinished(eventEndTimeParsed)) {
            changeCardState()
        }

        val eventDateAndPlaceText = TIME_FORMAT.format(
            eventApiData.startTime?.substring(TIME_STRING_START, TIME_STRING_END),
            eventApiData.endTime?.substring(TIME_STRING_START, TIME_STRING_END),
            eventApiData.place
        )

        eventDateAndPlaceTextView.text = eventDateAndPlaceText

        speakerName.text = eventApiData.speaker?.fullName ?: "null"
        speakerJob.text = eventApiData.speaker?.job
        eventTitle.text = eventApiData.title

    }

    fun onViewRecycled(){
        favoriteEventActionObservable.unsubscribe(favoriteObserver)
    }


    private fun changeCardState() {
        branchEvent.setBackgroundResource(R.drawable.bg_events_card_focus_disabled)

        eventState.visibility = View.VISIBLE
        eventState.text = itemView.resources.getString(R.string.event_state_text_view_finished)
        eventState.setBackgroundResource(R.drawable.bg_event_state_finished)
//        branchEvent.alpha = 0.5F - alternative background
//        eventState.alpha= 1F
    }

    private fun isEventFinished(eventEndTime : ZonedDateTime): Boolean {
        val currentDateAndTime = ZonedDateTime.now().toString()
        val currentTimeDateParsed = ZonedDateTime.parse(currentDateAndTime)

        return currentTimeDateParsed.isAfter(eventEndTime)
    }

    private fun setImageResourceState(
            isFavorite: Boolean,
            favoriteImageView: ImageView
    ) {
        when (isFavorite) {
            true -> favoriteImageView.setImageResource(R.drawable.ic_favorite_fill)
            false -> favoriteImageView.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun getFavoriteImageResource(
        isFavorite: Boolean
    ): Int = when (isFavorite) {
        true -> R.drawable.ic_favorite_fill
        else -> R.drawable.ic_favorite_border
    }
}