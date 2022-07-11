package kz.kolesateam.confapp.favorite_events.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.models.ProgressState
import kz.kolesateam.confapp.utils.CoroutineViewModel

class FavoriteEventsViewModel(
    private val favoriteEventsRepository: FavoriteEventsRepository
) : CoroutineViewModel(Dispatchers.Main) {

    private val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    private val favoriteEventsLiveData: MutableLiveData<List<EventApiData>> = MutableLiveData()
    private val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getProgressLiveData(): LiveData<ProgressState> = progressLiveData
    fun getFavoriteEventsLiveData(): LiveData<List<EventApiData>> = favoriteEventsLiveData
    fun getErrorLiveData(): LiveData<String> = errorLiveData

    fun onStart() {
        getFavoriteEvents()
    }

    private fun getFavoriteEvents() {
        launch {
            progressLiveData.value = ProgressState.Loading
            val favoriteEventsResponse = withContext(Dispatchers.IO) {
                favoriteEventsRepository.getAllFavoriteEvents()
            }
            if (favoriteEventsResponse is ResponseData.Success) {
                favoriteEventsLiveData.value = favoriteEventsResponse.result

            } else {
                val errorResponse = favoriteEventsResponse as ResponseData.Error
                errorLiveData.value = errorResponse.toString()
            }
            progressLiveData.value = ProgressState.Done
        }
    }
}