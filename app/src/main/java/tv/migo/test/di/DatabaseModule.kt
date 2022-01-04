package tv.migo.test.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tv.migo.test.db.MigoDatabase
import tv.migo.test.db.dao.PassDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MigoDatabase {
        return MigoDatabase.init(context)
    }

    @Provides
    fun providePassDao(database: MigoDatabase): PassDao {
        return database.passDao()
    }
}