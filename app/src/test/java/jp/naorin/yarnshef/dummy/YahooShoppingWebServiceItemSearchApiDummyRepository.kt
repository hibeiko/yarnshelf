package jp.naorin.yarnshef.dummy

import jp.naorin.yarnshef.data.YahooShoppingWebServiceItemDummyData
import jp.naorin.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.naorin.yarnshelf.repository.YahooShoppingWebServiceItemSearchApiRepository

class YahooShoppingWebServiceItemSearchApiDummyRepository : YahooShoppingWebServiceItemSearchApiRepository {
    override suspend fun searchItem(
        janCode: String,
        query: String
    ): YahooShoppingWebServiceItemData {
        if( query == "Error")
            throw Exception()
        else
            return YahooShoppingWebServiceItemDummyData.dummyData
    }
}