package expr.secminhr.cavern.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import expr.secminhr.cavern.article.Article
import expr.secminhr.cavern.articleinfo.ArticleInfo
import kotlinx.datetime.LocalDateTime

@Composable
fun ArticleScreen(info: ArticleInfo, article: Article?) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        ArticleInfoItem(info)
        HorizontalDivider()
        if (article != null) {
            Text(article.content)
        } else {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleScreenPreview() {
    ArticleScreen(ArticleInfo(
        0,
        "Title",
        "Author",
        "Name",
        LocalDateTime(2021, 5, 1, 0, 0),
        3,
        3,
        true
    ), Article(
        "Author",
        "Name",
        "Title",
        "Content",
        LocalDateTime(2021, 5, 1, 0, 0),
        3,
        3,
        true
    ))
}

@Preview(showBackground = true)
@Composable
fun ArticleScreenLoadingPreview() {
    ArticleScreen(ArticleInfo(
        0,
        "Title",
        "Author",
        "Name",
        LocalDateTime(2021, 5, 1, 0, 0),
        3,
        3,
        true
    ), null)
}