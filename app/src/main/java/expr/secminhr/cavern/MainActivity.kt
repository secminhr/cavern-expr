package expr.secminhr.cavern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import expr.secminhr.cavern.articleinfo.ArticleInfo
import expr.secminhr.cavern.articleinfo.ArticleInfoListRepo
import expr.secminhr.cavern.network.ArticleInfoListNetworkJsonSource
import expr.secminhr.cavern.serialize.ArticleInfoListJsonFetcher
import expr.secminhr.cavern.ui.theme.CavernTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
            CavernTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {
                        ArticleInfoList(Modifier.padding(it), viewModel.infoListPager.flow)
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleInfoList(
    modifier: Modifier = Modifier,
    pagingFlow: Flow<PagingData<ArticleInfo>>
) {
    val infoList = pagingFlow.collectAsLazyPagingItems()
    LazyColumn(modifier = modifier) {
        items(infoList.itemCount) {
            val info = infoList[it]
            if (info != null) {
                ArticleInfoItem(info)
            }
            Divider()
        }
    }
}

@Composable
fun ArticleInfoItem(articleInfo: ArticleInfo) {
    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = articleInfo.title, style = MaterialTheme.typography.bodyLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = articleInfo.authorName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Icon(
            Icons.Default.ThumbUp, contentDescription = "like",
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleInfoListPreview() {
    val list = buildList {
        for (i in 1..5) {
            add(ArticleInfo(
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
            ))
        }
    }
    ArticleInfoList(pagingFlow = MutableStateFlow(PagingData.from(list)))
}

@Preview(showBackground = true)
@Composable
fun ArticleInfoItemPreview() {
    ArticleInfoItem(
        ArticleInfo(
            "5",
            "title",
            "authorUsername", "author",
            LocalDateTime(
                year = 2024,
                month = Month(2),
                dayOfMonth = 25,
                hour = 22,
                minute = 28,
                second = 35
            ),
            10, 4, false
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ArticleInfoItemLongTitlePreview() {
    ArticleInfoItem(
        ArticleInfo(
            "5",
            "Soooooooooooooooooooooooooooooo loooooooooong title",
            "authorUsername", "author",
            LocalDateTime(
                year = 2024,
                month = Month(2),
                dayOfMonth = 25,
                hour = 22,
                minute = 28,
                second = 35
            ),
            10, 4, false
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ArticleInfoItemLongNumber() {
    ArticleInfoItem(
        ArticleInfo(
            "5",
            "title",
            "authorUsername", "author",
            LocalDateTime(
                year = 2024,
                month = Month(2),
                dayOfMonth = 25,
                hour = 22,
                minute = 28,
                second = 35
            ),
            100, 400, false
        )
    )
}



