package expr.secminhr.cavern

import expr.secminhr.cavern.article.Article
import expr.secminhr.cavern.serialize.ArticleJsonFetcher
import expr.secminhr.cavern.serialize.ArticleJsonSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleFetcherTest {
    @Test
    fun testJsonFetcher() = runTest {
        val jsonSource = mockk<ArticleJsonSource>()
        coEvery { jsonSource.getArticle(any(Int::class)) } returns buildJsonObject {
            put("fetch", 12345)
            putJsonObject("post") {
                put("author", "authorUsername")
                put("name", "authorDisplayName")
                put("title", "post title")
                put("content", "post content")
                put("time", "2024-01-29 21:42:35")
                put("likes_count", "10")
                put("comments_count", "4")
                put("islike", true)
            }
        }

        val fetcher = ArticleJsonFetcher(jsonSource)
        val article = fetcher.fetch(5)

        coVerify(exactly = 1) { jsonSource.getArticle(5) }
        assertEquals(Article(
            authorId = "authorUsername", authorName = "authorDisplayName",
            title = "post title", content = "post content",
            postDate = LocalDateTime(2024, 1, 29, 21, 42, 35),
            commentsNumber = 4, likesNumber = 10, userLiked = true
        ), article)
    }
}