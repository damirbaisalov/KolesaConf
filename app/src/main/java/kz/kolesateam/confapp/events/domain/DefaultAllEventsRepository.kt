package kz.kolesateam.confapp.events.domain

import kz.kolesateam.confapp.events.data.datasource.EventsDataSource
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem
import java.lang.Exception

class DefaultAllEventsRepository(
        private val eventsDataSource: EventsDataSource
): AllEventsRepository {

    override fun getBranchEvents(id: Int): ResponseData<List<UpcomingEventListItem>, String> {
       return try {
            val response = eventsDataSource.getBranchEvents(id).execute()
            if (response.isSuccessful) {
                val eventListItemList: List<UpcomingEventListItem> = response.body()!!.map {
                    eventApiData ->
                    UpcomingEventListItem(
                            type = DATA_TYPE_SECOND,
                            data = eventApiData
                    )
                }

                ResponseData.Success(eventListItemList)
            } else {
                ResponseData.Error("Response is not successful")
            }
       } catch (e: Exception) {
            ResponseData.Error(e.localizedMessage ?: "Exception occurred")
        }
    }
}