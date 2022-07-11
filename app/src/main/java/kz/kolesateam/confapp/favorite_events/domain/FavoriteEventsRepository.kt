package kz.kolesateam.confapp.favorite_events.domain

import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData
import java.util.*

interface FavoriteEventsRepository {

    fun saveFavoriteEvent(
            eventApiData: EventApiData
    )

    fun removeFavoriteEvent(
            eventId: Int?
    )

    fun getAllFavoriteEvents(): ResponseData<List<EventApiData>, String>

    fun isFavorite(id: Int?): Boolean
}