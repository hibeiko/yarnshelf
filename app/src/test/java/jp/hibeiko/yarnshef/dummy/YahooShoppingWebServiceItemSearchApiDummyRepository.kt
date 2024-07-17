package jp.hibeiko.yarnshef.dummy

import jp.hibeiko.yarnshef.data.YahooShoppingWebServiceItemDummyData
import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.hibeiko.yarnshelf.repository.YahooShoppingWebServiceItemSearchApiRepository

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