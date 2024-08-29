package jp.naorin.yarnshelf.network

import jp.naorin.yarnshelf.data.YahooShoppingWebServiceItemData
import retrofit2.http.GET
import retrofit2.http.Query

interface YahooShoppingWebServiceItemSearchApiService {
    @GET("itemSearch")
    suspend fun searchItem(
        @Query("appid") appId: String,
        @Query("jan_code") janCode: String,
        @Query("query") query: String,
        @Query("genre_category_id") genreCategoryId: Int,
        @Query("sort") sort: String,
    ): YahooShoppingWebServiceItemData
}
