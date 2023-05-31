package bd.com.albin.tasks.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTasksDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, TasksDatabase::class.java, "tasks").build()

    @Singleton
    @Provides
    fun provideTaskDao(db: TasksDatabase) = db.taskDao()
}