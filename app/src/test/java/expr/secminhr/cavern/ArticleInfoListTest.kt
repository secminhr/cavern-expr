package expr.secminhr.cavern

import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListRepo
import expr.secminhr.cavern.articleinfo.ArticleInfoListFetcher
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleInfoListTest {
    @Test
    fun incrementalFetchArticleInfo() = runTest {
        val fetcher = mockk<ArticleInfoListFetcher>()
        val info = listOf(mockk<ArticleInfo>(), mockk<ArticleInfo>(), mockk<ArticleInfo>(), mockk<ArticleInfo>())
        coEvery { fetcher.fetch(any()) } answers {
            if (arg<Int>(0) == 1) {
                info.slice(0..1)
            } else {
                info.slice(2..3)
            }
        }

        val articleInfoListRepo = ArticleInfoListRepo(fetcher)
        articleInfoListRepo.fetchNextPage()
        articleInfoListRepo.fetchNextPage()

        coVerify {
            fetcher.fetch(1)
            fetcher.fetch(2)
        }

        assertEquals(info, articleInfoListRepo.infoList)
    }

    @Test
    fun testUpdateListener() = runTest {
        val fetcher = mockk<ArticleInfoListFetcher>()
        val info = listOf(mockk<ArticleInfo>(), mockk<ArticleInfo>(), mockk<ArticleInfo>(), mockk<ArticleInfo>())
        coEvery { fetcher.fetch(any()) } answers {
            if (arg<Int>(0) == 1) {
                info.slice(0..1)
            } else {
                info.slice(2..3)
            }
        }

        val articleInfoListRepo = ArticleInfoListRepo(fetcher)
        val list = mutableListOf<ArticleInfo>()
        articleInfoListRepo.addInfoListUpdateListener {
            list.addAll(it)
        }

        articleInfoListRepo.fetchNextPage()
        articleInfoListRepo.fetchNextPage()

        assertEquals(articleInfoListRepo.infoList, list)
    }
}