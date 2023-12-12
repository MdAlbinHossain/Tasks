package bd.com.albin.tasks.data.model

import bd.com.albin.tasks.data.local.TaskEntity

data class Task(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val priority: String = "",
    val dueDate: Long = 0L,
    val dueTime: Long = 0L,
    val remind: Boolean = false,
    val repeat: String = "",
    val completed: Boolean = false,
)
fun Task.asEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    priority = priority,
    dueDate = dueDate,
    dueTime = dueTime,
    remind= remind,
    repeat = repeat,
    completed=completed,
)