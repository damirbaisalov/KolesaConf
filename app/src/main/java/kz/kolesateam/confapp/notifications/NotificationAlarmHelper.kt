package kz.kolesateam.confapp.notifications

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kz.kolesateam.confapp.events.domain.models.EventApiData
import org.threeten.bp.ZonedDateTime

const val NOTIFICATION_CONTENT_KEY = "notification_title"
const val NOTIFICATION_ID_KEY = "notification_id"

class NotificationAlarmHelper(
        private val application: Application
) {
    fun createNotificationAlarm(
            eventData: EventApiData
    ) {
        val alarmManager: AlarmManager? = application.getSystemService(
                Context.ALARM_SERVICE
        ) as? AlarmManager

        val pendingIntent: PendingIntent = Intent(application, NotificationAlarmBroadcastReceiver::class.java).apply {
            putExtra(NOTIFICATION_CONTENT_KEY, eventData.title)
            putExtra(NOTIFICATION_ID_KEY, eventData.id)
        }.let {
            PendingIntent.getBroadcast(application, 0, it,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val convertedStartTime = ZonedDateTime.parse(eventData.startTime)
        val startTimeInMilliseconds: Long = convertedStartTime.toEpochSecond()*1000
        alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                startTimeInMilliseconds,
                pendingIntent
        )
    }

    fun cancelNotificationAlarm(){
        val alarmManager = application.getSystemService(
                Context.ALARM_SERVICE
        ) as AlarmManager

        val pendingIntent: PendingIntent = Intent(application,NotificationAlarmBroadcastReceiver::class.java).let {
            PendingIntent.getBroadcast(application, 0, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        alarmManager.cancel(pendingIntent)
    }
}