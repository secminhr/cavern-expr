package expr.secminhr.cavern.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import expr.secminhr.cavern.ArticleInfoListViewModel
import expr.secminhr.cavern.articleinfo.ArticleInfo
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month


@Composable
fun ArticleInfoList(
    modifier: Modifier = Modifier,
    pagingFlow: ArticleInfoListViewModel.AutoFetchList,
    listState: LazyListState = rememberLazyListState(),
    onItemClicked: (ArticleInfo) -> Unit = {}
) {
    LazyColumn(modifier = modifier, state = listState) {
        items(pagingFlow.count) { index ->
            val info = pagingFlow[index]
            ArticleInfoItem(info) {
                onItemClicked(info)
            }
        }
    }
}

@Composable
fun ArticleInfoItem(articleInfo: ArticleInfo, onItemClicked: (() -> Unit)? = null) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = onItemClicked?.let { Modifier.clickable { it() } } ?: Modifier
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(vertical = 12.dp)
            .padding(start = 16.dp)) {
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
                .padding(vertical = 12.dp)
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleInfoListPreview() {
    val list = buildList {
        for (i in 1..5) {
            add(
                ArticleInfo(
                i,
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
//    ArticleInfoList(pagingFlow = MutableStateFlow(PagingData.from(list)))
    ArticleInfoList(pagingFlow = ArticleInfoListViewModel.AutoFetchList(null, list.toMutableList()) { })
}

@Preview(showBackground = true)
@Composable
fun ArticleInfoItemPreview() {
    ArticleInfoItem(
        ArticleInfo(
            5,
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
            5,
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
            5,
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