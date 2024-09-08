package expr.secminhr.cavern.ui

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import expr.secminhr.cavern.article.Article
import expr.secminhr.cavern.articleinfo.ArticleInfo


@Composable
fun ArticleScreen(article: Article?) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxWidth()) {
        if (article == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            ArticleInfoItem(article.extractInfo())
            ArticleContent(article.content, MaterialTheme.colorScheme.background, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

fun Article.extractInfo(): ArticleInfo {
    return ArticleInfo(
        articleId = 0,  // filling with dummy value, won't be used anyway
        title = title,
        authorId = authorId,
        authorName = authorName,
        postDate = postDate,
        commentsNumber = commentsNumber,
        likesNumber = likesNumber,
        userLiked = userLiked
    )
}

@Composable
fun ArticleContent(content: String, backgroundColor: Color, modifier: Modifier = Modifier, onUserLinkClicked: (String) -> Unit = {}) {
    // copy from https://github.com/secminhr/TheNewCavern/blob/master/app/src/main/java/personal/secminhr/cavern/main/ui/views/markdown/MarkdownView.kt
    // which in terms a modified version of cavern's website markdown renderer
    val markdownRenderHTML = "<!doctype html>\n" +
            "<html>\n" +
            " <head> \n" +
            "  <meta charset=\"UTF-8\"> \n" +
            "  <!-- Tocas UI：CSS 與元件 --> \n" +
            "  <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/tocas-ui/2.3.3/tocas.css\"> \n" +
            "  <!-- Fonts --> \n" +
            "  <link href=\"https://fonts.googleapis.com/css2?family=Open+Sans&amp;display=swap\" rel=\"stylesheet\"> \n" +
            "  <link href=\"https://fonts.googleapis.com/css2?family=Source+Code+Pro&amp;display=swap\" rel=\"stylesheet\"> \n" +
            "  <!-- jQuery --> \n" +
            "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.slim.min.js\"></script> \n" +
            "  <link rel=\"stylesheet\" href=                                                                                                                                                                                                                                      \"https://pandao.github.io/editor.md/css/editormd.css\"> \n" +
            "  <link rel=\"stylesheet\" href=\"https://stoneapp.tech/cavern/include/css/cavern.css\"> \n" +
            " </head> \n" +
            " <body>     \n" +
            "  <div class=\"ts flatted segment\" id=\"post\">  \n" +
            "   <div class=\"markdown\" style=\"display:None\">{{markdown_replace}}\n" +
            "   </div>\n" +
            "  </div>\n" +
            "  <script src=\"https://unpkg.com/load-js@1.2.0\"></script>\n" +
            "  <script src=\"https://stoneapp.tech/cavern/include/js/lib/editormd.js\"></script>\n" +
            "  <script src=\"https://stoneapp.tech/cavern/include/js/markdown.js\"></script>\n" +
            "  <script src=\"https://stoneapp.tech/cavern/include/js/post.js\"></script>\n" +
            "  <style> #post { background: {{background_color}}; } </style>" +
            " </body>\n" +
            "</html>"

    lateinit var markdown: String

    AndroidView(factory = {
        WebView(it).apply {
            settings.javaScriptEnabled = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            markdown = markdownRenderHTML
                .replace("{{markdown_replace}}", content)
                .replace("{{background_color}}", backgroundColor.toHashString())
            loadDataWithBaseURL(
                "https://stoneapp.tech/cavern",
                markdown,
                "text/html",
                "utf-8",
                ""
            )
            setBackgroundColor(backgroundColor.toArgb())
        }.apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url?.toString()
                    url?.let {
                        return if (it.startsWith("https://stoneapp.tech/user.php?username=")) {
                            onUserLinkClicked(it.drop("https://stoneapp.tech/user.php?username=".length))
                            true
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = request.url
                            context.startActivity(intent)
                            true
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
        }
    }, modifier = modifier)
}

private fun Color.toHashString(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return "#${red.toString(16)}${green.toString(16)}${blue.toString(16)}"
}