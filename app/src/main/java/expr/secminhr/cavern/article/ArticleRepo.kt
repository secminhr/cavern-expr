package expr.secminhr.cavern.article

class ArticleRepo(private val fetcher: ArticleFetcher) {
    suspend fun getArticle(articleId: Int): Article = fetcher.fetch(articleId)
}
