package tv.migo.test.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import tv.migo.test.repo.PassRepoImpl
import tv.migo.test.repo.PassRepository
import tv.migo.test.repo.StatusRepoImpl
import tv.migo.test.repo.StatusRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {

    @ViewModelScoped
    @PassRepo
    @Binds
    abstract fun bindPassRepository(impl: PassRepoImpl): PassRepository

    @ViewModelScoped
    @StatusRepo
    @Binds
    abstract fun bindStatusRepository(impl: StatusRepoImpl): StatusRepository
}