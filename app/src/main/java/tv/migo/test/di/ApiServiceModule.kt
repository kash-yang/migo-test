package tv.migo.test.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tv.migo.test.api.ApiServices
import tv.migo.test.api.services.MigoStatusService

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {

    @Provides
    fun provideMigoStatusService(apiServices: ApiServices): MigoStatusService {
        return apiServices.migoStatusService
    }
}