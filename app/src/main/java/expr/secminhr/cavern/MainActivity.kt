package expr.secminhr.cavern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import expr.secminhr.cavern.article.Article
import expr.secminhr.cavern.article.ArticleRepo
import expr.secminhr.cavern.articleinfo.ArticleInfoListRepo
import expr.secminhr.cavern.network.ArticleInfoListNetworkJsonSource
import expr.secminhr.cavern.network.ArticleNetworkJsonSource
import expr.secminhr.cavern.serialize.ArticleInfoListJsonFetcher
import expr.secminhr.cavern.serialize.ArticleJsonFetcher
import expr.secminhr.cavern.ui.ArticleInfoList
import expr.secminhr.cavern.ui.ArticleScreen
import expr.secminhr.cavern.ui.theme.CavernTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class MainActivity : ComponentActivity() {
    private val viewModel: ArticleInfoListViewModel by viewModels<ArticleInfoListViewModel> {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val articleInfoListRepo = ArticleInfoListRepo(ArticleInfoListJsonFetcher(
                    ArticleInfoListNetworkJsonSource(HttpClient(CIO))
                ))

                return ArticleInfoListViewModel(articleInfoListRepo) as T
            }
        }
    }

    private val navState = NavigationState()
    private val articleRepo = ArticleRepo(ArticleJsonFetcher(
        ArticleNetworkJsonSource(HttpClient(CIO))
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CavernTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavFrame(
                        backToList = navState::backToList,
                        showingBackButton = navState.showingBackButton,
                        actions = {
                            IconButton({ navState.enterLoginPage() }) {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = "login button"
                                )
                            }
                        }
                    ) {
                        val listState = rememberLazyListState()  // preserve across screen changes
                        Crossfade(modifier = Modifier.padding(it), targetState = navState.currentScreen) { screen ->
                            when (screen) {
                                NavigationState.Screen.ArticleInfoList -> {
                                    ArticleInfoList(pagingFlow = viewModel.infoList, listState = listState, onItemClicked = navState::articleInfoClicked)
                                }
                                NavigationState.Screen.Article -> {
                                    var article by remember { mutableStateOf<Article?>(null) }
                                    ArticleScreen(article)
                                    LaunchedEffect(navState.showingArticleInfo) {
                                        article = articleRepo.getArticle(navState.showingArticleInfo!!.articleId)
                                    }
                                }
                                NavigationState.Screen.Login -> {
                                    Text("Login")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavFrame(backToList: () -> Unit = {}, showingBackButton: Boolean, actions: @Composable RowScope.() -> Unit = { }, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Cavern", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    if (showingBackButton) {
                        IconButton(backToList) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "back to list"
                            )
                        }
                    }
                },
                actions = actions
            )
        },
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun NavFramePreviewInfoList() {
    NavFrame(showingBackButton = false) {
        Text("ArticleInfoList")
    }
}

@Preview(showBackground = true)
@Composable
fun NavFramePreviewArticle() {
    NavFrame(showingBackButton = true) {
        Text("Article")
    }
}