package expr.secminhr.cavern

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListRepo

class MainActivityViewModel(private val articleInfoListRepo: ArticleInfoListRepo): ViewModel() {
    val infoListPager = Pager(
        PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            ArticleInfoListPagingSource(articleInfoListRepo)
        }
    )

    private class ArticleInfoListPagingSource(private val articleInfoListRepo: ArticleInfoListRepo): PagingSource<Int, ArticleInfo>() {
        override fun getRefreshKey(state: PagingState<Int, ArticleInfo>): Int? {
            return null
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleInfo> {
            val list = articleInfoListRepo.fetchNextPage()

            return LoadResult.Page(
                data = list,
                nextKey = (params.key ?: 1) + 1,
                prevKey = params.key?.let { it - 1 }
            )
        }
    }
}