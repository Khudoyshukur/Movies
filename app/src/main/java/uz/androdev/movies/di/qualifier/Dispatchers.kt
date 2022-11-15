package uz.androdev.movies.di.qualifier

import javax.inject.Qualifier

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 7:21 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher