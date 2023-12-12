package bd.com.albin.tasks.data.repository

import bd.com.albin.tasks.data.local.TaskEntity
import bd.com.albin.tasks.data.local.TasksDao
import bd.com.albin.tasks.data.local.asExternalModel
import bd.com.albin.tasks.data.model.Task
import bd.com.albin.tasks.data.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalTasksRepository @Inject constructor(private val tasksDao: TasksDao) : TasksRepository {

    override fun getAllTasksStream(): Flow<List<Task>> =
        tasksDao.getAllTasksStream().map { taskEntityList ->
            taskEntityList.map(TaskEntity::asExternalModel)
        }

    override fun getTaskStream(id: Int): Flow<Task?> =
        tasksDao.getTaskStream(id).map { it?.asExternalModel() }

    override suspend fun insertTask(task: Task) = tasksDao.insertTask(task.asEntity())

    override suspend fun deleteTask(task: Task) = tasksDao.deleteTask(task.asEntity())

    override suspend fun updateTask(task: Task) = tasksDao.updateTask(task.asEntity())
}