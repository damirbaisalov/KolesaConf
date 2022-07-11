package kz.kolesateam.confapp.events.domain

import kz.kolesateam.confapp.events.data.datasource.EventsDataSource
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem

const val DATA_TYPE_SECOND = 2

class DefaultUpcomingEventsRepository(
    private val eventsDataSource: EventsDataSource
): UpcomingEventsRepository {

    override fun getUpcomingEvents(): ResponseData<List<UpcomingEventListItem>, String> {
        return try {
            val response = eventsDataSource.getUpcomingEvents().execute()
            if (response.isSuccessful) {
                val branchListItemList: List<UpcomingEventListItem> = response.body()!!.map {
                    branchApiData ->
                    UpcomingEventListItem(
                            type = DATA_TYPE_SECOND,
                            data = branchApiData
                    )
                }
                ResponseData.Success(branchListItemList)
            } else {
                 ResponseData.Error("Response is not successful")
            }
        } catch (e: Exception) {
             ResponseData.Error(e.localizedMessage ?: "Exception occurred")
        }
    }
}