package kz.kolesateam.confapp.favorite_events.data

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.MapType
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.ResponseData
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.favorite_events.domain.model.FavoriteActionEvent
import java.io.FileInputStream
import java.io.FileOutputStream

private const val FAVORITE_FILE_NAME = "favorite_events.json"

class DefaultFavoriteEventsRepository(
        private val context: Context,
        private val objectMapper: ObjectMapper,
        private val favoriteActionEventActionObservable: FavoriteEventActionObservable
): FavoriteEventsRepository {

    private var favoriteEvents: MutableMap<Int, EventApiData> = mutableMapOf()

    init {
            val favoriteEventsFromFile: Map<Int, EventApiData> = getFavoritesFromFile()
            favoriteEvents = mutableMapOf()
            favoriteEvents.putAll(favoriteEventsFromFile)
    }

    override fun saveFavoriteEvent(eventApiData: EventApiData) {
        eventApiData.id ?: return

        favoriteEvents[eventApiData.id] = eventApiData
        saveFavoriteEventsToFile()
        favoriteActionEventActionObservable.notifyChanged(
            eventId = eventApiData.id,
            isFavorite = true
        )
    }

    override fun removeFavoriteEvent(eventId: Int?) {
        favoriteEvents.remove(eventId)
        saveFavoriteEventsToFile()
        favoriteActionEventActionObservable.notifyChanged(
            eventId = eventId!!,
            isFavorite = false
        )
    }

    override fun getAllFavoriteEvents(): ResponseData<List<EventApiData>, String> {
        return ResponseData.Success(
                result = favoriteEvents.values.toList()
        )
    }

    override fun isFavorite(id: Int?): Boolean = favoriteEvents.containsKey(id)

    private fun saveFavoriteEventsToFile() {
        val favoriteEventsJsonString: String = objectMapper.writeValueAsString(favoriteEvents)
        val fileOutputStream: FileOutputStream = context.openFileOutput(
                FAVORITE_FILE_NAME,
                Context.MODE_PRIVATE
        )
        fileOutputStream.write(favoriteEventsJsonString.toByteArray())
        fileOutputStream.close()
    }

    private fun getFavoritesFromFile(): Map<Int, EventApiData> {
        var fileInputStream: FileInputStream?= null

        try {
            fileInputStream = context.openFileInput(FAVORITE_FILE_NAME)
        } catch (exception: Exception) {
            fileInputStream?.close()

            return emptyMap()
        }
        val favoriteEventsJsonString: String = fileInputStream?.bufferedReader()?.readLines()?.joinToString().orEmpty()
        val mapType: MapType = objectMapper.typeFactory.constructMapType(Map::class.java, Int::class.java, EventApiData::class.java)

        return objectMapper.readValue(favoriteEventsJsonString, mapType)
    }
}