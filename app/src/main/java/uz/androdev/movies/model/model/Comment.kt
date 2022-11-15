package uz.androdev.movies.model.model

import org.threeten.bp.LocalDateTime

/**
 * Created by: androdev
 * Date: 15-11-2022
 * Time: 8:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class Comment(
    val id: Long,
    val comment: String,
    val createdAt: LocalDateTime
)