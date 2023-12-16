package bd.com.albin.tasks.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import bd.com.albin.tasks.data.local.TaskEntity
import bd.com.albin.tasks.data.local.TasksDao
import bd.com.albin.tasks.data.local.asExternalModel
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.data.model.asEntity
import bd.com.albin.tasks.domain.worker.TaskReminderWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocalTasksRepository @Inject constructor(private val tasksDao: TasksDao, context: Context) :
    TasksRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun getAllTasksStream(): Flow<List<Task>> =
        tasksDao.getAllTasksStream().map { taskEntityList ->
            taskEntityList.map(TaskEntity::asExternalModel)
        }

    override fun getTaskStream(id: Int): Flow<Task?> =
        tasksDao.getTaskStream(id).map { it?.asExternalModel() }

    override suspend fun insertTask(task: Task) {
        val taskEntity = task.asEntity()
        tasksDao.insertTask(taskEntity)
        if (taskEntity.remind) scheduleReminder(taskEntity.asExternalModel())
    }

    override suspend fun deleteTask(task: Task) {
        cancelReminder(task)
        tasksDao.deleteTask(task.asEntity())
    }

    override suspend fun updateTask(task: Task) {
        tasksDao.updateTask(task.asEntity())
        if (task.remind) scheduleReminder(task)
        else cancelReminder(task)
    }

    private fun scheduleReminder(task: Task) {
        val currentMoment: Instant = Clock.System.now()
        val taskInstant =
            Instant.fromEpochMilliseconds(task.dueDate).plus(task.dueTime, DateTimeUnit.MINUTE)
                .toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.currentSystemDefault())
        val diff: Long = taskInstant.toEpochMilliseconds() - currentMoment.toEpochMilliseconds()

        val data = Data.Builder()

        data.putInt(TaskReminderWorker.TASK_ID_KEY, task.id)

        val workRequestBuilder = OneTimeWorkRequestBuilder<TaskReminderWorker>().setInitialDelay(
            diff, TimeUnit.MILLISECONDS
        ).setInputData(data.build()).build()

        workManager.enqueueUniqueWork(
            task.id.toString(), ExistingWorkPolicy.REPLACE, workRequestBuilder
        )

    }

    private fun cancelReminder(task: Task) {
        workManager.cancelUniqueWork(task.id.toString())
    }
}