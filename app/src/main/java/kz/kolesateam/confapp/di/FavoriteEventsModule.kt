package kz.kolesateam.confapp.di

import kz.kolesateam.confapp.favorite_events.data.DefaultFavoriteEventsRepository
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.favorite_events.presentation.FavoriteEventsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val favoriteEventsModule: Module = module {

    viewModel {
        FavoriteEventsViewModel(
            favoriteEventsRepository = get()
        )
    }

    single  {
        DefaultFavoriteEventsRepository(
                context = androidApplication(),
                objectMapper = get(),
                favoriteActionEventActionObservable = get()
        ) as FavoriteEventsRepository
    }

    single {
        FavoriteEventActionObservable()
    }
}