package expr.secminhr.cavern.serialize

import expr.secminhr.cavern.article.Article
import expr.secminhr.cavern.article.ArticleFetcher
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ArticleJsonFetcher(private val jsonSource: ArticleJsonSource): ArticleFetcher {
    @Serializable
    private data class FetchResult(
        val post: ArticleResult
    )

    @Serializable
    private data class ArticleResult(
        val author: String, val name: String, val title: String, val content: String,
        val time: String, val likes_count: String, val comments_count: String, val islike: Boolean
    ) {
        fun toArticle() = Article(
            authorId = author, authorName = name, title, content,
            postDate = time.replace(' ', 'T').toLocalDateTime(),
            commentsNumber = comments_count.toInt(), likesNumber = likes_count.toInt(),
            userLiked = islike
        )
    }

    override suspend fun fetch(articleId: Int) =
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<FetchResult>(jsonSource.getArticle(articleId)).post.toArticle()
}
