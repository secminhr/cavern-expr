package expr.secminhr.cavern

import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListFetcher
import expr.secminhr.cavern.serialize.ArticleInfoListJsonFetcher
import expr.secminhr.cavern.serialize.ArticleInfoListJsonSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleInfoListFetcherTest {
    @Test
    fun testJsonFetcherParsing() = runTest {
        val jsonSource = mockk<ArticleInfoListJsonSource>()
        coEvery { jsonSource.getArticleInfo(any()) } returns buildJsonObject {
            put("page", 1)
            putJsonArray("posts") {
                for (i in 1..5) {
                    addJsonObject {
                        put("author", "authorUsername${i}")
                        put("name", "author${i}")
                        put("pid", i)
                        put("title", "title${i}")
                        put("time", "2024-0${i}-25 22:28:35")
                        put("likes_count", "$i")
                        put("comments_count", "${i * 2}")
                        put("islike", i % 2 == 0)
                    }
                }
            }
        }

        val fetcher: ArticleInfoListFetcher = ArticleInfoListJsonFetcher(jsonSource)
        val articlesInfo = fetcher.fetch(0)
        val expectedArticleInfo = buildList {
            for (i in 1..5) {
                add(
                    ArticleInfo(
                    i,
                    "title${i}",
                    "authorUsername${i}", "author${i}",
                    LocalDateTime(
                        year = 2024, month = Month(i), dayOfMonth = 25, hour = 22, minute = 28, second = 35
                    ),
                    i * 2, i, i % 2 == 0
                )
                )
            }
        }
        assertEquals(expectedArticleInfo, articlesInfo)
    }
}