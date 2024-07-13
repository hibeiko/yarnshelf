package jp.hibeiko.yarnshef.repository

import jp.hibeiko.yarnshef.data.YahooShoppingWebServiceItemDummyData
import jp.hibeiko.yarnshef.data.YahooShoppingWebServiceItemSearchDummyApiService
import jp.hibeiko.yarnshelf.repository.YahooShoppingWebServiceItemSearchApiRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class YahooShoppingWebServiceItemSearchApiRepositoryTest {
    @Test
    fun yahooShoppingWebServiceItemSearchApiRepository_searchItem_verifyItemList() =
        // コルーチン テスト ライブラリには runTest() 関数が用意されています。この関数は、ラムダで渡されたメソッドを受け取り、TestScope（CoroutineScope から継承）から実行します。
        runTest {
            val repository = YahooShoppingWebServiceItemSearchApiRepositoryImpl(
                yahooShoppingWebServiceItemSearchApiService = YahooShoppingWebServiceItemSearchDummyApiService(),
                appId = ""
            )
            assertEquals(
                YahooShoppingWebServiceItemDummyData.dummyData,
                repository.searchItem("", "")
            )
        }
}