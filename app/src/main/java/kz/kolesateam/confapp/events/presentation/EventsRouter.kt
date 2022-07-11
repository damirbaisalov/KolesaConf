package kz.kolesateam.confapp.events.presentation

import android.app.Application
import android.content.Context
import android.content.Intent
import kz.kolesateam.confapp.events_details.presentation.EventDetailsActivity

const val PUSH_NOTIFICATION_MESSAGE = "push_message"
const val PUSH_NOTIFICATION_EVENT_ID = "push_event_id"

class EventsRouter {

    fun createIntent(
            application: Application
    ): Intent = Intent(application, EventDetailsActivity::class.java)

    fun createIntentForNotification(
            application: Application,
            messageFromPush: String,
            eventId: Int
    ): Intent = createIntent(application).apply {
        putExtra(PUSH_NOTIFICATION_MESSAGE, messageFromPush)
        putExtra(PUSH_NOTIFICATION_EVENT_ID, eventId)
    }
}