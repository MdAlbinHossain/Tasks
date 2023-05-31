

package bd.com.albin.tasks.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 3, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
