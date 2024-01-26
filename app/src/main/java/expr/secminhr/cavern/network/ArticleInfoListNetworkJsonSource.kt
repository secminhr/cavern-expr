package expr.secminhr.cavern.network

import expr.secminhr.cavern.serialize.ArticleInfoListJsonSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class ArticleInfoListNetworkJsonSource(private val client: HttpClient): ArticleInfoListJsonSource {
    override suspend fun getArticleInfo(page: Int): JsonObject {
        val result: String = client.get("https://stoneapp.tech/cavern/ajax/posts.php?page=$page").body()
        return Json.decodeFromString(result)
    }
}