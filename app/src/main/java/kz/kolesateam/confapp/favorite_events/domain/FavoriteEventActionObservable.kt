package kz.kolesateam.confapp.favorite_events.domain

import kz.kolesateam.confapp.favorite_events.domain.model.FavoriteActionEvent
import java.util.*

class FavoriteEventActionObservable: Observable() {

    fun subscribe(favoritesObserver: Observer) {
        addObserver(favoritesObserver)
    }

    fun unsubscribe(favoritesObserver: Observer) {
        deleteObserver(favoritesObserver)
    }

    fun notifyChanged(eventId: Int, isFavorite: Boolean) {
        notify(
            FavoriteActionEvent(
                eventId = eventId,
                isFavorite = isFavorite
            )
        )
    }

    fun notify(favoriteActionEvent: FavoriteActionEvent) {
        setChanged()
        notifyObservers(favoriteActionEvent)
    }

}