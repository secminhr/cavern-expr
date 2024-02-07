package expr.secminhr.cavern.article

import kotlinx.datetime.LocalDateTime

data class Article(val authorId: String, val authorName: String, val title: String, val content: String,
                   val postDate: LocalDateTime,
                   val commentsNumber: Int, val likesNumber: Int, val userLiked: Boolean)
