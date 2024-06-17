package jp.hibeiko.yarnshelf.repository

import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.hibeiko.yarnshelf.network.YahooShoppingWebServiceItemSearchApiService

interface YahooShoppingWebServiceItemSearchApiRepository {
    suspend fun searchItem(
        janCode: String
    ): YahooShoppingWebServiceItemData
}

class YahooShoppingWebServiceItemSearchApiRepositoryImpl(
    private  val yahooShoppingWebServiceItemSearchApiService: YahooShoppingWebServiceItemSearchApiService,
    private val appId: String
) : YahooShoppingWebServiceItemSearchApiRepository{
    override suspend fun searchItem( janCode: String) : YahooShoppingWebServiceItemData{
        return yahooShoppingWebServiceItemSearchApiService.searchItem(appid = appId, janCode = janCode)
    }
}