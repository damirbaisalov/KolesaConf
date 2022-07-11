package kz.kolesateam.confapp.events.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.kolesateam.confapp.events.domain.AllEventsRepository
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.models.ProgressState
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.utils.CoroutineViewModel

class AllEventsViewModel(
    private val allEventsRepository: AllEventsRepository,
    private val favoriteEventsRepository: FavoriteEventsRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper
): CoroutineViewModel(Dispatchers.Main) {

    private val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val allEventsLiveData: MutableLiveData<List<UpcomingEventListItem>> = MutableLiveData()
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getProgressLiveData(): LiveData<ProgressState> = progressLiveData
    fun getAllEventsLiveData(): LiveData<List<UpcomingEventListItem>> = allEventsLiveData
    fun getErrorLiveData(): LiveData<String> = errorLiveData

    fun onStart(
        branchId: Int,
        branchTitle: String
    ) {
        loadBranchEvents(
            branchId = branchId,
            branchTitle = branchTitle
        )
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

    private fun loadBranchEvents(
        branchId: Int,
        branchTitle: String
    ) {
        launch {
            progressLiveData.value = ProgressState.Loading
            val branchEventsResponse = withContext(Dispatchers.IO) {
                allEventsRepository.getBranchEvents(branchId)
            }
            if (branchEventsResponse is ResponseData.Success) {
                allEventsLiveData.value = prepareAllEventsList(
                    branchEventsResponse.result,
                    branchTitle = branchTitle
                )
            } else {
                val errorResponse = branchEventsResponse as ResponseData.Error
                errorLiveData.value = errorResponse.toString()
            }
            progressLiveData.value = ProgressState.Done
        }
    }

    private fun prepareAllEventsList(
        allEvents: List<UpcomingEventListItem>,
        branchTitle: String
    ): List<UpcomingEventListItem> {
        val allEventListItemDataList: MutableList<UpcomingEventListItem> = mutableListOf()
        val branchTitleData = UpcomingEventListItem(
            type = DATA_TYPE_FIRST,
            data = branchTitle
        )
        allEventListItemDataList.add(branchTitleData)

        allEvents.forEach {
            val eventApiData: EventApiData = it.data as? EventApiData ?: return@forEach
            eventApiData.isFavorite = favoriteEventsRepository.isFavorite(eventApiData.id)
        }
        allEventListItemDataList.addAll(allEvents)

        return allEventListItemDataList
    }
}