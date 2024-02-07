package expr.secminhr.cavern.serialize

import kotlinx.serialization.json.JsonObject

interface ArticleJsonSource {
    suspend fun getArticle(articleId: Int): JsonObject
}
