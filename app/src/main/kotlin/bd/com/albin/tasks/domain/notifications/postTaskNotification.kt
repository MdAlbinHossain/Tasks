package bd.com.albin.tasks.domain.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import bd.com.albin.tasks.R
import bd.com.albin.tasks.data.model.Task
import dagger.hilt.android.qualifiers.ApplicationContext

private const val TARGET_ACTIVITY_NAME = "bd.com.albin.tasks.TasksActivity"
private const val TASK_NOTIFICATION_REQUEST_CODE = 0
private const val TASK_NOTIFICATION_CHANNEL_ID = ""
private const val TASK_NOTIFICATION_GROUP = "TASK_NOTIFICATIONS"
private const val DEEP_LINK_SCHEME_AND_HOST = "https://www.tasks.albin.com.bd"
private const val FOR_YOU_PATH = "foryou"

fun postTaskNotifications(
    task: Task, @ApplicationContext context: Context
) = with(context) {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS,
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    val taskNotification = createTaskNotification {
        setSmallIcon(
            R.drawable.ic_launcher_foreground,
        ).setContentTitle(task.title).setContentText(task.description)
            .setContentIntent(taskPendingIntent(task)).setGroup(TASK_NOTIFICATION_GROUP)
            .setAutoCancel(true)
    }

    // Send the notification
    val notificationManager = NotificationManagerCompat.from(this)
    notificationManager.notify(task.id.hashCode(), taskNotification)
}


private fun Context.createTaskNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        TASK_NOTIFICATION_CHANNEL_ID,
    ).setPriority(NotificationCompat.PRIORITY_DEFAULT).apply(block).build()
}

private fun Context.ensureNotificationChannelExists() {

    val channel = NotificationChannel(
        TASK_NOTIFICATION_CHANNEL_ID,
        getString(R.string.app_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(R.string.app_name)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.taskPendingIntent(
    task: Task,
): PendingIntent? = PendingIntent.getActivity(
    this,
    TASK_NOTIFICATION_REQUEST_CODE,
    Intent().apply {
        action = Intent.ACTION_VIEW
        data = task.taskDeepLinkUri()
        component = ComponentName(
            packageName,
            TARGET_ACTIVITY_NAME,
        )
    },
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
)

private fun Task.taskDeepLinkUri() = "$DEEP_LINK_SCHEME_AND_HOST/$FOR_YOU_PATH/$id".toUri()