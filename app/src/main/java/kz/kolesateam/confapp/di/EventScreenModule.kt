package kz.kolesateam.confapp.di

import kz.kolesateam.confapp.events.data.datasource.EventsDataSource
import kz.kolesateam.confapp.events.domain.AllEventsRepository
import kz.kolesateam.confapp.events.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.events.domain.DefaultAllEventsRepository
import kz.kolesateam.confapp.events.domain.DefaultUpcomingEventsRepository
import kz.kolesateam.confapp.events.presentation.AllEventsViewModel
import kz.kolesateam.confapp.events.presentation.UpcomingEventsViewModel
import kz.kolesateam.confapp.events_details.data.DefaultEventDetailsRepository
import kz.kolesateam.confapp.events_details.domain.EventDetailsRepository
import kz.kolesateam.confapp.events_details.presentation.EventDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

private const val BASE_URL = "http://37.143.8.68:2020"

val eventScreenModule: Module = module {

    viewModel {
        UpcomingEventsViewModel(
            upcomingEventsRepository = get(),
            favoriteEventsRepository = get(),
            userNameDataSource = get(named(SHARED_PREFS_DATA_SOURCE)),
            notificationAlarmHelper = get()
        )
    }

    viewModel {
        AllEventsViewModel(
            allEventsRepository = get(),
            favoriteEventsRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    viewModel {
        EventDetailsViewModel(
            eventDetailsRepository = get(),
            favoriteEventsRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    single {
        val retrofit: Retrofit = get()

        retrofit.create(EventsDataSource::class.java)
    }

    factory {
        DefaultUpcomingEventsRepository(
            eventsDataSource = get()
        ) as UpcomingEventsRepository
    }

    factory {
        DefaultAllEventsRepository(
            eventsDataSource = get()
        ) as AllEventsRepository
    }

    factory {
        DefaultEventDetailsRepository(
            eventsDataSource = get()
        ) as EventDetailsRepository
    }
}