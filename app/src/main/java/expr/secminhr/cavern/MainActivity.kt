package expr.secminhr.cavern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListRepo
import expr.secminhr.cavern.network.ArticleInfoListNetworkJsonSource
import expr.secminhr.cavern.serialize.ArticleInfoListJsonFetcher
import expr.secminhr.cavern.ui.ArticleInfoList
import expr.secminhr.cavern.ui.theme.CavernTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel> {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val client = HttpClient(CIO)
                val articleInfoListSource = ArticleInfoListNetworkJsonSource(client)
                val articleInfoListFetcher = ArticleInfoListJsonFetcher(articleInfoListSource)
                val articleInfoListRepo = ArticleInfoListRepo(articleInfoListFetcher)
                return MainActivityViewModel(articleInfoListRepo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentShowingArticleInfo: ArticleInfo? by remember {
                mutableStateOf(null)
            }

            CavernTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavFrame(listPagerFlow = viewModel.infoList) {
                        currentShowingArticleInfo = it
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavFrame(listPagerFlow: MainActivityViewModel.AutoFetchList, onArticleItemClicked: (ArticleInfo) -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Cavern", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ))
        }
    ) {
        ArticleInfoList(Modifier.padding(it), listPagerFlow) {
            onArticleItemClicked(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavFramePreview() {
    val list = buildList {
        for (i in 1..5) {
            add(
                ArticleInfo(
                    "$i",
                    "title${i}",
                    "authorUsername${i}", "author${i}",
                    LocalDateTime(
                        year = 2024,
                        month = Month(i),
                        dayOfMonth = 25,
                        hour = 22,
                        minute = 28,
                        second = 35
                    ),
                    i * 2, i, i % 2 == 0
                )
            )
        }
    }
//    NavFrame(MutableStateFlow(PagingData.from(list)))
    NavFrame(MainActivityViewModel.AutoFetchList(null, list.toMutableList()) { })
}



