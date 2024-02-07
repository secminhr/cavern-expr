package expr.secminhr.cavern.serialize

import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListFetcher
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ArticleInfoListJsonFetcher(private val jsonSource: ArticleInfoListJsonSource):
    ArticleInfoListFetcher {
    @Serializable
    private data class ArticleInfoListJsonSerializable(
        val posts: List<ArticleInfoJsonSerializable>
    )

    @Serializable
    private data class ArticleInfoJsonSerializable(
        private val author: String,
        private val name: String,
        private val pid: Int,
        private val title: String,
        private val time: String,
        private val likes_count: String,
        private val comments_count: String,
        private val islike: Boolean
    ) {
        fun toArticleInfo(): ArticleInfo = ArticleInfo(
            pid, title, author, name,
            //replace the space between date and time by T to make it iso
            time.replace(' ', 'T').toLocalDateTime(),
            comments_count.toInt(), likes_count.toInt(), islike
        )
    }

    override suspend fun fetch(page: Int): List<ArticleInfo> =
        Json { ignoreUnknownKeys = true }
            .decodeFromJsonElement<ArticleInfoListJsonSerializable>(jsonSource.getArticleInfo(page)).posts
            .map(ArticleInfoJsonSerializable::toArticleInfo)
}
