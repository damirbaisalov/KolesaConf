package kz.kolesateam.confapp.di

import kz.kolesateam.confapp.events.data.datasource.UserNameDataSource
import kz.kolesateam.confapp.events.data.datasource.UserNameMemoryDataSource
import kz.kolesateam.confapp.events.data.datasource.UserNameSharedPrefsDataSource
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SHARED_PREFS_DATA_SOURCE = "shared_prefs_data_source"
const val MEMORY_DATA_SOURCE = "memory_data_source"

val userNameModule: Module = module {

    factory(named(SHARED_PREFS_DATA_SOURCE)) {
        UserNameSharedPrefsDataSource(
            sharedPreferences = get()
        ) as UserNameDataSource
    }

    single(named(MEMORY_DATA_SOURCE)) {
        UserNameMemoryDataSource() as UserNameDataSource
    }
}