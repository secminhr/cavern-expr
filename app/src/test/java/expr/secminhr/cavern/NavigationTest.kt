package expr.secminhr.cavern

import expr.secminhr.cavern.articleinfo.ArticleInfo
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class NavigationTest {
    @Test
    fun testStart() {
        val navState = NavigationState()

        assertEquals(navState.currentScreen, NavigationState.Screen.ArticleInfoList)
    }

    @Test
    fun testArticleInfoListItemClickedShowsArticle() {
        val navState = NavigationState()
        val info = mockk<ArticleInfo>()
        navState.articleInfoClicked(info)

        assertEquals(navState.currentScreen, NavigationState.Screen.Article)
        assertEquals(navState.showingArticleInfo, info)
        assertTrue(navState.showingBackButton)
    }

    @Test
    fun testBackButtonToArticleInfoList() {
        val navState = NavigationState()
        val info = mockk<ArticleInfo>()
        navState.articleInfoClicked(info)
        navState.backToList()

        assertEquals(navState.currentScreen, NavigationState.Screen.ArticleInfoList)
    }
}