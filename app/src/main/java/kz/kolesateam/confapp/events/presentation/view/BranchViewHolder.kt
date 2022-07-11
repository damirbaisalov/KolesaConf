package kz.kolesateam.confapp.events.presentation.view

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.BranchApiData
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.favorite_events.domain.model.FavoriteActionEvent
import java.util.*

const val TIME_FORMAT = "%s - %s • %s"
const val TIME_STRING_START = 11
const val TIME_STRING_END = 16

class BranchViewHolder(
    itemView: View,
    private val branchClickListener: BranchClickListener,
    private val favoriteClickListener: FavoriteClickListener,
    private val eventClickListener: EventClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable
) : RecyclerView.ViewHolder(itemView) {

    private val favoriteObserver: Observer = object: Observer {
        override fun update(o: Observable?, favoriteEventActionObject: Any?) {
            val favoriteActionEvent = (favoriteEventActionObject as? FavoriteActionEvent) ?: return

            if (branchData.events?.isEmpty()!!) return

            val firstEvent = branchData.events?.first()
            val lastEvent = branchData.events?.last()

            if (firstEvent?.id == favoriteActionEvent.eventId) {
                favoriteImageViewCurrent.setImageResource(
                    getFavoriteImageResource(favoriteActionEvent.isFavorite)
                )
            }
             else if (lastEvent?.id == favoriteActionEvent.eventId) {
                favoriteImageViewNext.setImageResource(
                    getFavoriteImageResource(favoriteActionEvent.isFavorite)
                )
            }
        }
    }

    private val branchCurrentEvent: View = itemView.findViewById(R.id.branch_current_event)
    private val branchNextEvent: View = itemView.findViewById(R.id.branch_next_event)

    private val branchTitle: TextView = itemView.findViewById(R.id.branch_title)
    private val branchLineTitle: LinearLayout = itemView.findViewById(R.id.branch_line)

    private val eventDateAndPlaceCurrent: TextView = branchCurrentEvent.findViewById(R.id.time_start_end_place)
    private val speakerNameCurrent: TextView = branchCurrentEvent.findViewById(R.id.speaker_name)
    private val speakerJobCurrent: TextView = branchCurrentEvent.findViewById(R.id.job)
    private val eventTitleCurrent: TextView = branchCurrentEvent.findViewById(R.id.event_title)
    private val favoriteImageViewCurrent: ImageView = branchCurrentEvent.findViewById(R.id.ic_favorite)

    private val eventDateAndPlaceNext: TextView = branchNextEvent.findViewById(R.id.time_start_end_place)
    private val speakerNameNext: TextView = branchNextEvent.findViewById(R.id.speaker_name)
    private val speakerJobNext: TextView = branchNextEvent.findViewById(R.id.job)
    private val eventTitleNext: TextView = branchNextEvent.findViewById(R.id.event_title)
    private val favoriteImageViewNext: ImageView = branchNextEvent.findViewById(R.id.ic_favorite)

    private lateinit var branchData: BranchApiData

    init {
        val currentEventState: TextView = branchCurrentEvent.findViewById(R.id.event_state)
        currentEventState.visibility = View.INVISIBLE
    }

    fun onBind(branchApiData: BranchApiData){

        branchData = (branchApiData as? BranchApiData) ?: return

        branchTitle.text = branchApiData.title
        branchLineTitle.setOnClickListener {
            branchClickListener.onBranchClick(branchApiData.id!!, branchApiData.title.toString())
        }

        if (branchApiData.events?.isEmpty()!!){
            branchCurrentEvent.visibility = View.GONE
            branchNextEvent.visibility = View.GONE

            return
        }

        val currentEvent: EventApiData = branchApiData.events.first()
        val nextEvent: EventApiData = branchApiData.events.last()

        if (currentEvent.isFavorite) {
            favoriteImageViewCurrent.setImageResource(R.drawable.ic_favorite_fill)
        }

        if (nextEvent.isFavorite) {
            favoriteImageViewNext.setImageResource(R.drawable.ic_favorite_fill)
        }

        favoriteImageViewCurrent.setOnClickListener {
            currentEvent.isFavorite = !currentEvent.isFavorite
            setImageResourceState(currentEvent.isFavorite, favoriteImageViewCurrent)
            favoriteClickListener.onClick(currentEvent)
        }

        favoriteImageViewNext.setOnClickListener {
            nextEvent.isFavorite = !nextEvent.isFavorite
            setImageResourceState(nextEvent.isFavorite, favoriteImageViewNext)
            favoriteClickListener.onClick(nextEvent)
        }

        branchCurrentEvent.setOnClickListener {
            eventClickListener.onClick(currentEvent.id!!)
        }

        branchNextEvent.setOnClickListener {
            eventClickListener.onClick(nextEvent.id!!)
        }

        val currentEventDateAndPlaceText = TIME_FORMAT.format(
                branchApiData.events.first().startTime?.substring(TIME_STRING_START, TIME_STRING_END),
                branchApiData.events.first().endTime?.substring(TIME_STRING_START, TIME_STRING_END),
                branchApiData.events.first().place
        )

        eventDateAndPlaceCurrent.text = currentEventDateAndPlaceText
        speakerNameCurrent.text = currentEvent.speaker?.fullName ?: "null"
        speakerJobCurrent.text = currentEvent.speaker?.job
        eventTitleCurrent.text = currentEvent.title

        val nextEventDateAndPlaceText = TIME_FORMAT.format(
                branchApiData.events.last().startTime?.substring(TIME_STRING_START, TIME_STRING_END),
                branchApiData.events.last().endTime?.substring(TIME_STRING_START, TIME_STRING_END),
                branchApiData.events.last().place
        )

        eventDateAndPlaceNext.text = nextEventDateAndPlaceText
        speakerNameNext.text = nextEvent.speaker?.fullName ?: "null"
        speakerJobNext.text = nextEvent.speaker?.job
        eventTitleNext.text = nextEvent.title

        favoriteEventActionObservable.subscribe(favoriteObserver)
    }

    fun onViewRecycled(){
        favoriteEventActionObservable.unsubscribe(favoriteObserver)
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