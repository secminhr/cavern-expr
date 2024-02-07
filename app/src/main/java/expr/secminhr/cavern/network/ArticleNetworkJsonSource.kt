package expr.secminhr.cavern.network

import expr.secminhr.cavern.serialize.ArticleJsonSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class ArticleNetworkJsonSource(private val client: HttpClient): ArticleJsonSource {
    override suspend fun getArticle(articleId: Int): JsonObject {
        val result: String = client.get("https://stoneapp.tech/cavern/ajax/posts.php?pid=$articleId").body()
        return Json.decodeFromString(result)
    }
}