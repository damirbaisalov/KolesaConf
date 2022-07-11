package kz.kolesateam.confapp.events_details.domain

import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData

interface EventDetailsRepository {

    fun getEventDetails(
            id: Int
    ) : ResponseData<EventApiData, String>
}