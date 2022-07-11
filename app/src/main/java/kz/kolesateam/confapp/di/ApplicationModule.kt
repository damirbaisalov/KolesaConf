package kz.kolesateam.confapp.di

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

private const val APPLICATION_SHARED_PREFERENCES = "application"

val applicationModule: Module = module {

    single {
        val context = androidApplication()

        context.getSharedPreferences(APPLICATION_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        ObjectMapper()
    }

    single {
        NotificationAlarmHelper(
                application = androidApplication()
        )
    }
}