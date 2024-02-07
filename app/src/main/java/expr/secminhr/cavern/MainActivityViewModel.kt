package expr.secminhr.cavern

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import expr.secminhr.cavern.article.Article
import expr.secminhr.cavern.article.ArticleRepo
import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListRepo
import kotlinx.coroutines.launch

class MainActivityViewModel(private val articleInfoListRepo: ArticleInfoListRepo, private val articleRepo: ArticleRepo): ViewModel() {
    class AutoFetchList(
        articleInfoListRepo: ArticleInfoListRepo?,
        private val list: MutableList<ArticleInfo> = mutableStateListOf(),
        private val fetchNext: () -> Unit) {

        var count by mutableIntStateOf(list.size)
            private set

        init {
            articleInfoListRepo?.addInfoListUpdateListener {
                list.addAll(it)
                count = list.size
            }
        }

        operator fun get(index: Int): ArticleInfo {
            if (list.size - index <= 15) {
                fetchNext()
            }

            return list[index]
        }
    }

    val infoList = AutoFetchList(articleInfoListRepo, fetchNext = ::fetchNextPage)
    init {
        fetchNextPage()
    }

    private fun fetchNextPage() {
        viewModelScope.launch { articleInfoListRepo.fetchNextPage() }
    }

    var article: Article? by mutableStateOf(null)
    fun viewArticle(info: ArticleInfo) {
        viewModelScope.launch {
            article = articleRepo.getArticle(info.articleId)
        }
    }
}