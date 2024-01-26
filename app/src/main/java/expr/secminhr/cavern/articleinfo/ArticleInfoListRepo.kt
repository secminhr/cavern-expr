package expr.secminhr.cavern.articleinfo

class ArticleInfoListRepo(private val fetcher: ArticleInfoListFetcher) {
    private val _infoList: MutableList<ArticleInfo> = mutableListOf()
    val infoList: List<ArticleInfo>
        get() = _infoList.toList()

    private val onInfoListUpdateListener = mutableListOf<(List<ArticleInfo>) -> Unit>()
    fun addInfoListUpdateListener(listener: (List<ArticleInfo>) -> Unit) {
        onInfoListUpdateListener.add(listener)
    }
    private fun onInfoListUpdate(nextPartial: List<ArticleInfo>) {
        onInfoListUpdateListener.forEach { it(nextPartial) }
    }

    private var nextPage = 1

    suspend fun fetchNextPage() {
        val nextList = fetcher.fetch(nextPage++)
        _infoList += nextList
        onInfoListUpdate(nextList)
    }
}
