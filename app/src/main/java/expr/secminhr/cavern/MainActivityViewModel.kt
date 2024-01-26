package expr.secminhr.cavern

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListRepo
import kotlinx.coroutines.launch

class MainActivityViewModel(private val articleInfoListRepo: ArticleInfoListRepo): ViewModel() {
    val infoList = mutableStateListOf<ArticleInfo>()
    init {
        articleInfoListRepo.addInfoListUpdateListener {  nextPartial ->
            infoList.addAll(nextPartial)
        }
    }

    fun onLoadButtonClicked() {
        viewModelScope.launch {
            articleInfoListRepo.fetchNextPage()
        }
    }
}