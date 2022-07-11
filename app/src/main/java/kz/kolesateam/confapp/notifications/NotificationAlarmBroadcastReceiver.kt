package kz.kolesateam.confapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationAlarmBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val content: String = intent?.getStringExtra(NOTIFICATION_CONTENT_KEY).orEmpty()
        val eventId: Int = intent?.getIntExtra(NOTIFICATION_ID_KEY, 0)!!
        NotificationHelper.sendNotification(
                title = "Не пропустите следущий доклад",
                content = content,
                eventId = eventId
        )
    }
}