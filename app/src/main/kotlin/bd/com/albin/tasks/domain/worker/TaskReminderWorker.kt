package bd.com.albin.tasks.domain.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bd.com.albin.tasks.domain.notifications.postTaskNotification
import bd.com.albin.tasks.domain.usecases.GetTaskStreamUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getTaskStreamUseCase: GetTaskStreamUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val plantName = inputData.getInt(TASK_ID_KEY, 0)
        val task = getTaskStreamUseCase(plantName).first()

        if (task != null) {
            postTaskNotification(
                task,
                applicationContext
            )
        }

        return Result.success()
    }

    companion object {
        const val TASK_ID_KEY = "TASK_ID_KEY"
    }
}