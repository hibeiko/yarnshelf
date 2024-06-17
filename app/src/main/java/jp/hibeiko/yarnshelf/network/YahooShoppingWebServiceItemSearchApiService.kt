package jp.hibeiko.yarnshelf.network

import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import retrofit2.http.GET
import retrofit2.http.Query

interface YahooShoppingWebServiceItemSearchApiService {
    @GET("itemSearch")
    suspend fun searchItem(
        @Query("appid") appId: String,
        @Query("jan_code") janCode: String
    ): YahooShoppingWebServiceItemData
}
