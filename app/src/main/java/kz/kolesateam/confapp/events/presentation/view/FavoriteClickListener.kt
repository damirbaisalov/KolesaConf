package kz.kolesateam.confapp.events.presentation.view

import kz.kolesateam.confapp.events.domain.models.EventApiData

interface FavoriteClickListener {

    fun onClick(eventData: EventApiData)
}