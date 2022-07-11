package kz.kolesateam.confapp.favorite_events.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.di.favoriteEventsModule
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.presentation.view.EventClickListener

class FavoriteEventsViewHolder(
        itemView: View,
        private val eventClickListener: EventClickListener
): RecyclerView.ViewHolder(itemView) {

    private val eventState: TextView = itemView.findViewById(R.id.event_state)

    private val eventDateAndPlaceTextView: TextView = itemView.findViewById(R.id.time_start_end_place)
    private val speakerName: TextView = itemView.findViewById(R.id.speaker_name)
    private val speakerJob: TextView = itemView.findViewById(R.id.job)
    private val eventTitle: TextView = itemView.findViewById(R.id.event_title)
    private val favoriteImageView: ImageView = itemView.findViewById(R.id.ic_favorite)

    init {
        eventState.visibility = View.INVISIBLE
    }

    fun onBind(eventApiData: EventApiData) {


        if (eventApiData.isFavorite)
            favoriteImageView.setImageResource(R.drawable.ic_favorite_fill)

        itemView.setOnClickListener {
            eventClickListener.onClick(eventApiData.id!!)
        }

        val eventDateAndPlaceText = "%s - %s • %s".format(
                eventApiData.startTime?.substring(11,16),
                eventApiData.endTime?.substring(11,16),
                eventApiData.place
        )

        eventDateAndPlaceTextView.text = eventDateAndPlaceText

        speakerName.text = eventApiData.speaker?.fullName ?: "null"
        speakerJob.text = eventApiData.speaker?.job
        eventTitle.text = eventApiData.title
    }
}