package bd.com.albin.tasks.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import bd.com.albin.tasks.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("SELECT * FROM TaskEntity ORDER BY dueDate ASC")
    fun getAllTasksStream(): Flow<List<TaskEntity>>

    @Query("SELECT * from TaskEntity WHERE id = :id")
    fun getTaskStream(id: Int): Flow<TaskEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)
}