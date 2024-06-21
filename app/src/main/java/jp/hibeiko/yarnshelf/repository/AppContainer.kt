package jp.hibeiko.yarnshelf.repository

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import jp.hibeiko.yarnshelf.data.YarnShelfDatabase
import jp.hibeiko.yarnshelf.network.YahooShoppingWebServiceItemSearchApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val yarnDataRepository: YarnDataRepository
    val yahooShoppingWebServiceItemSearchApiRepository: YahooShoppingWebServiceItemSearchApiRepository
}

/**
 * [AppContainer] implementation that provides instance of [YarnDataRepositoryImpl]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [YarnDataRepository]
     */
    override val yarnDataRepository: YarnDataRepository by lazy {
        YarnDataRepositoryImpl(YarnShelfDatabase.getDatabase(context).yarnDataDAO())
    }

    // 商品検索（v3）URL
// APIドキュメントは以下
// https://developer.yahoo.co.jp/webapi/shopping/v3/itemsearch.html
    private val baseUrl =
        "https://shopping.yahooapis.jp/ShoppingWebService/V3/"
    // Client ID（アプリケーションID）
    private val appId =
        "dj00aiZpPVREZVo5ZmlOQXJqVSZzPWNvbnN1bWVyc2VjcmV0Jng9MzY-"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        // Retrofit は、ウェブサービスのベース URI と、ウェブサービス API を構築するためのコンバータ ファクトリを必要とします。
        // コンバータは、ウェブサービスから返されたデータをどのように処理するかを Retrofit に伝えます。

        // 文字列とその他のプリミティブ型をサポートする ScalarsConverterを利用します。
//    .addConverterFactory(ScalarsConverterFactory.create())

        // kotlinx.serialization コンバータを使用して、JSON オブジェクトを Kotlin オブジェクトに変換します。
        // ignoreUnknownKeys...入力JSON内の未知のプロパティに遭遇した場合に、無視するか、例外を発生ないかどうか。
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: YahooShoppingWebServiceItemSearchApiService by lazy {
        retrofit.create(YahooShoppingWebServiceItemSearchApiService::class.java)
    }

    // lazy=遅延初期化により、プロパティが最初に使用されるときに初期化されるようにします。
    override val yahooShoppingWebServiceItemSearchApiRepository: YahooShoppingWebServiceItemSearchApiRepository by lazy{
        YahooShoppingWebServiceItemSearchApiRepositoryImpl(retrofitService,appId)
    }
}