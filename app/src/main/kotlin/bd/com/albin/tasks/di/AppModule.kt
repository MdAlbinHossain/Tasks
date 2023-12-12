package bd.com.albin.tasks.di

import android.content.Context
import androidx.room.Room
import bd.com.albin.tasks.data.local.TasksDatabase
import bd.com.albin.tasks.data.repository.LocalTasksRepository
import bd.com.albin.tasks.data.repository.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesTasksDatabase(@ApplicationContext context: Context): TasksDatabase =
        Room.databaseBuilder(
            context = context, klass = TasksDatabase::class.java, name = "tasks_db"
        ).build()

    @Provides
    @Singleton
    fun providesTasksRepository(tasksDatabase: TasksDatabase): TasksRepository =
        LocalTasksRepository(tasksDatabase.tasksDao)

}