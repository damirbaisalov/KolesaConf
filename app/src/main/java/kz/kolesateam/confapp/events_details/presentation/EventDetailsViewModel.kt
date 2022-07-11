package kz.kolesateam.confapp.events_details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events_details.domain.EventDetailsRepository
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.models.ProgressState
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.utils.CoroutineViewModel

class EventDetailsViewModel(
    private val eventDetailsRepository: EventDetailsRepository,
    private val favoriteEventsRepository: FavoriteEventsRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper
): CoroutineViewModel(Dispatchers.Main) {

    private val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val eventDetailsLiveData: MutableLiveData<EventApiData> = MutableLiveData()
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getProgressLiveData(): LiveData<ProgressState> = progressLiveData
    fun getEventDetailsLiveData(): LiveData<EventApiData> = eventDetailsLiveData
    fun getErrorLiveData(): LiveData<String> = errorLiveData

    fun onStart(id: Int) {
        getEventDetails(id = id)
    }

    fun onFavoriteClick(
        eventData: EventApiData
    ) {
        when(eventData.isFavorite) {
            true -> {
                favoriteEventsRepository.saveFavoriteEvent(eventData)
                scheduleEvent(eventData)
            }
            else -> {
                favoriteEventsRepository.removeFavoriteEvent(eventId = eventData.id)
                cancelScheduleEvent()
            }
        }
    }

    private fun scheduleEvent(eventData: EventApiData) {
        notificationAlarmHelper.createNotificationAlarm(
            eventData = eventData
        )
    }

    private fun cancelScheduleEvent() {
        notificationAlarmHelper.cancelNotificationAlarm()
    }

    private fun getEventDetails(id: Int) {
        launch {
            progressLiveData.value = ProgressState.Loading
            val eventDetailsResponse = withContext(Dispatchers.IO) {
                eventDetailsRepository.getEventDetails(id = id)
            }
            if (eventDetailsResponse is ResponseData.Success) {
                eventDetailsResponse.result.isFavorite = favoriteEventsRepository.isFavorite(id = id)
                eventDetailsLiveData.value = eventDetailsResponse.result
            } else {
                val errorResponse = eventDetailsResponse as ResponseData.Error
                errorLiveData.value = errorResponse.toString()
            }
            progressLiveData.value = ProgressState.Done
        }
    }
}