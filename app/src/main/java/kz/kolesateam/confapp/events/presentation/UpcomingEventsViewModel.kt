package kz.kolesateam.confapp.events.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.kolesateam.confapp.events.data.datasource.UserNameDataSource
import kz.kolesateam.confapp.events.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.events.domain.models.BranchApiData
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.models.ProgressState
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.utils.CoroutineViewModel

const val DATA_TYPE_FIRST = 1

class UpcomingEventsViewModel(
    private val upcomingEventsRepository: UpcomingEventsRepository,
    private val favoriteEventsRepository: FavoriteEventsRepository,
    private val userNameDataSource: UserNameDataSource,
    private val notificationAlarmHelper: NotificationAlarmHelper
): CoroutineViewModel(Dispatchers.Main) {

    private val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val upcomingEventsLiveData: MutableLiveData<List<UpcomingEventListItem>> = MutableLiveData()
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getProgressLiveData(): LiveData<ProgressState> = progressLiveData
    fun getUpcomingEventsLiveData(): LiveData<List<UpcomingEventListItem>> = upcomingEventsLiveData
    fun getErrorLiveData(): LiveData<String> = errorLiveData

    fun onStart() {
        getUpcomingEvents()
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

    private fun getUpcomingEvents() {
        launch {
            progressLiveData.value = ProgressState.Loading
            val upcomingEventsResponse = withContext(Dispatchers.IO) {
                upcomingEventsRepository.getUpcomingEvents()
            }
            if (upcomingEventsResponse is ResponseData.Success) {
                upcomingEventsLiveData.value = prepareUpcomingEventsList(
                    upcomingEventsResponse.result
                )
            } else {
                val errorResponse = upcomingEventsResponse as ResponseData.Error
                errorLiveData.value = errorResponse.toString()
            }
            progressLiveData.value = ProgressState.Done
        }
    }

    private fun prepareUpcomingEventsList(
        upcomingEvents: List<UpcomingEventListItem>
    ): List<UpcomingEventListItem> {
        val upcomingEventListItemDataList: MutableList<UpcomingEventListItem> = mutableListOf()
        val userName = getUserName()
        val helloUser = "Привет, $userName"
        val userNameData = UpcomingEventListItem(
            type = DATA_TYPE_FIRST,
            data = helloUser
        )
        upcomingEventListItemDataList.add(userNameData)

        upcomingEvents.forEach {
            val branchApiData: BranchApiData = it.data as? BranchApiData ?: return@forEach

            branchApiData.events?.forEach { eventApiData ->
                eventApiData.isFavorite = favoriteEventsRepository.isFavorite(eventApiData.id)
            }
        }
        upcomingEventListItemDataList.addAll(upcomingEvents)

        return upcomingEventListItemDataList
    }

    private fun getUserName(): String {
        return userNameDataSource.getSavedUserName()
    }
}