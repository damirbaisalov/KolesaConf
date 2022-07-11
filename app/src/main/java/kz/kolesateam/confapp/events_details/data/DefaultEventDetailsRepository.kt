package kz.kolesateam.confapp.events_details.data

import kz.kolesateam.confapp.events.data.datasource.EventsDataSource
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events_details.domain.EventDetailsRepository
import java.lang.Exception

class DefaultEventDetailsRepository(
        private val eventsDataSource: EventsDataSource
): EventDetailsRepository {

    override fun getEventDetails(id: Int): ResponseData<EventApiData, String> {
        return try {
            val response = eventsDataSource.getEventDetails(id).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()!!
                ResponseData.Success(responseBody)
            } else
            {
                ResponseData.Error("Response is not successful")
            }
        } catch (e: Exception) {
            ResponseData.Error(e.localizedMessage ?: "Exception occurred")
        }
    }
}