package uz.androdev.movies.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import uz.androdev.movies.di.qualifier.DefaultDispatcher
import uz.androdev.movies.di.qualifier.IODispatcher
import uz.androdev.movies.di.qualifier.MainDispatcher

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:21 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @IODispatcher
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDispatcher() = Dispatchers.Main

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher() = Dispatchers.Default
}