package jp.naorin.yarnshelf.repository

import jp.naorin.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.naorin.yarnshelf.network.YahooShoppingWebServiceItemSearchApiService

interface YahooShoppingWebServiceItemSearchApiRepository {
    suspend fun searchItem(
        janCode: String,
        query: String,
    ): YahooShoppingWebServiceItemData
}

class YahooShoppingWebServiceItemSearchApiRepositoryImpl(
    private val yahooShoppingWebServiceItemSearchApiService: YahooShoppingWebServiceItemSearchApiService,
    private val appId: String
) : YahooShoppingWebServiceItemSearchApiRepository {
    override suspend fun searchItem(
        janCode: String,
        query: String
    ): YahooShoppingWebServiceItemData {
        return yahooShoppingWebServiceItemSearchApiService.searchItem(
            appId = appId,
            janCode = janCode,
            query = query,
            genreCategoryId = 45251, // トップ> 楽器、手芸、コレクション> 手芸、ハンドクラフト> 編み物道具、毛糸> 毛糸
            sort = "-score"
            //並び順を指定
            //-score：おすすめ順
            //+price：価格の安い順
            //-price：価格の高い順
            //-review_count：商品レビュー数の多い順
            //※UTF-8にエンコードされている必要あり。
        )
    }
}