package kz.kolesateam.confapp.events.domain

import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem

interface UpcomingEventsRepository {

    fun getUpcomingEvents(): ResponseData<List<UpcomingEventListItem>, String>
}