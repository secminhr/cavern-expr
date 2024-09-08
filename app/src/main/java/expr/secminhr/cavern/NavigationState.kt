package expr.secminhr.cavern

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import expr.secminhr.cavern.articleinfo.ArticleInfo

class NavigationState {
    enum class Screen {
        ArticleInfoList,
        Article,
        Login
    }

    var showingArticleInfo: ArticleInfo? by mutableStateOf(null)
        private set
    var currentScreen by mutableStateOf(Screen.ArticleInfoList)
        private set

    val showingBackButton: Boolean
        get() = currentScreen != Screen.ArticleInfoList

    fun articleInfoClicked(info: ArticleInfo) {
        currentScreen = Screen.Article
        showingArticleInfo = info
    }

    fun backToList() {
        currentScreen = Screen.ArticleInfoList
    }

    fun enterLoginPage() {
        currentScreen = Screen.Login
    }
}
