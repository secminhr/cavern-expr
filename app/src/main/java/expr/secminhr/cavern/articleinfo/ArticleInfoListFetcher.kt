package expr.secminhr.cavern.articleinfo

interface ArticleInfoListFetcher {
    suspend fun fetch(page: Int): List<ArticleInfo>
}
