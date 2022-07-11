package kz.kolesateam.confapp.events.domain

import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem

interface AllEventsRepository {

    fun getBranchEvents(id: Int): ResponseData<List<UpcomingEventListItem>, String>
}