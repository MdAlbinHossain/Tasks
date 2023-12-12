package bd.com.albin.tasks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import bd.com.albin.tasks.data.model.Task


@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String = "",
    val description: String = "",
    val priority: String = "",
    val dueDate: Long = 0L,
    val dueTime: Long = 0L,
    val remind: Boolean = false,
    val repeat: String = "",
    val completed: Boolean = false,
)

fun TaskEntity.asExternalModel() = Task(
    id = id,
    title = title,
    description = description,
    priority = priority,
    dueDate = dueDate,
    dueTime = dueTime,
    remind = remind,
    repeat = repeat,
    completed = completed,
)