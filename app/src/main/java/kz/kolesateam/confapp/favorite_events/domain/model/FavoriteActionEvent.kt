package kz.kolesateam.confapp.favorite_events.domain.model

data class FavoriteActionEvent(
    val eventId: Int,
    val isFavorite: Boolean
)
