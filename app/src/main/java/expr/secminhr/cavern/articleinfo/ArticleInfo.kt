package expr.secminhr.cavern.articleinfo

import kotlinx.datetime.LocalDateTime

data class ArticleInfo(
    val articleId: Int,
    val title: String,
    val authorId: String,
    val authorName: String,
    val postDate: LocalDateTime,
    val commentsNumber: Int,
    val likesNumber: Int,
    val userLiked: Boolean
)
