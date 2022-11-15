package uz.androdev.movies.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.androdev.movies.data.repository.MovieRepository
import uz.androdev.movies.data.repository.impl.MovieRepositoryImpl

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
interface MovieRepositoryModule {
    @Binds
    fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}