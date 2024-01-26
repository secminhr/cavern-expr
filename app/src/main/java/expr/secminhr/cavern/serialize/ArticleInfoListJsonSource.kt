package expr.secminhr.cavern.serialize

import kotlinx.serialization.json.JsonObject

interface ArticleInfoListJsonSource {
    suspend fun getArticleInfo(page: Int): JsonObject
}
