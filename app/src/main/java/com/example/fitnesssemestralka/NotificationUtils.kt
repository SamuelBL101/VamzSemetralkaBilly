import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.fitnesssemestralka.R
import java.util.*
import java.util.concurrent.TimeUnit
import com.example.fitnesssemestralka.notificationTexts

/** Object with functions to creating and setting notifications
 *
 */
object NotificationUtils {
    private const val CHANNEL_ID = "NotificationChannel"
    private const val NOTIFICATION_ID = 1
    private val random = Random()

    /**
     * Create a notification channel for displaying notifications in an Android app.
     *
     * @param context
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Display a notification in an Android app.
     *
     * @param context
     * @param notificationText
     */
    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, notificationText: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.morningnotification)
            .setContentTitle(context.getString(R.string.FitnessJourney))
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    /**
     * Function is responsible for scheduling a notification to be shown at a later time.
     * @param context
     */
    fun scheduleNotification(context: Context) {
        val workManager = WorkManager.getInstance(context)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        val randomIndex = random.nextInt(notificationTexts.size)
        val notificationText = notificationTexts[randomIndex]

        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorkRequest)
    }

    /**
     * Function is responsible for calculating the initial delay in milliseconds before the notification should be shown.
     *
     * @return
     */
    private fun calculateInitialDelay(): Long {
        val calendar = Calendar.getInstance().apply {
            // Set the desired time for the notification to appear
            set(Calendar.HOUR_OF_DAY, 18) // Hour in 24-hour format
            set(Calendar.MINUTE, 9)
            set(Calendar.SECOND, 0)
        }

        val currentTime = Calendar.getInstance()
        if (calendar.before(currentTime)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        // Schedule for the next day if the desired time has already passed today
        }

        return calendar.timeInMillis - currentTime.timeInMillis
    }

    /**
     * Class that extends the Worker class from the WorkManager library.
     * It retrieves the application context, generates a random index, selects a random notification text,
     * and calls the showNotification() function.
     * @constructor
     * @param context
     * @param params
     */
    class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            val applicationContext = applicationContext
            val randomIndex = Random().nextInt(notificationTexts.size)
            val notificationText = notificationTexts[randomIndex]
            showNotification(applicationContext, notificationText)
            return Result.success()
        }
    }

    /**
     *  BroadcastReceiver that can be used to handle notification events triggered by the system or other components.
     *  It retrieves a random notification text and calls the showNotification() function.
     *
     */
    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val randomIndex = random.nextInt(notificationTexts.size)
            val notificationText = notificationTexts[randomIndex]
            showNotification(context, notificationText)
        }
    }
}
