package expr.secminhr.cavern.article

interface ArticleFetcher {
    suspend fun fetch(articleId: Int): Article
}
