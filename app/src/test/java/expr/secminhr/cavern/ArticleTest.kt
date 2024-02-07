package expr.secminhr.cavern

import expr.secminhr.cavern.article.Article
import expr.secminhr.cavern.article.ArticleFetcher
import expr.secminhr.cavern.article.ArticleRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleTest {
    @Test
    fun testFetchArticle() = runTest {
        val articleFetcher = mockk<ArticleFetcher>()
        val expectArticle = mockk<Article>()
        coEvery { articleFetcher.fetch(any(Int::class)) } returns expectArticle

        val articleRepo = ArticleRepo(articleFetcher)
        val article = articleRepo.getArticle(10)

        coVerify(exactly = 1) { articleFetcher.fetch(10) }

        assertEquals(expectArticle, article)
    }
}