package jp.hibeiko.yarnshef.data

import jp.hibeiko.yarnshelf.data.YahooBrand
import jp.hibeiko.yarnshelf.data.YahooDelivery
import jp.hibeiko.yarnshelf.data.YahooGenreCategory
import jp.hibeiko.yarnshelf.data.YahooHit
import jp.hibeiko.yarnshelf.data.YahooImage
import jp.hibeiko.yarnshelf.data.YahooParentBrands
import jp.hibeiko.yarnshelf.data.YahooParentGenreCategories
import jp.hibeiko.yarnshelf.data.YahooPoint
import jp.hibeiko.yarnshelf.data.YahooPriceLabel
import jp.hibeiko.yarnshelf.data.YahooRequest
import jp.hibeiko.yarnshelf.data.YahooReview
import jp.hibeiko.yarnshelf.data.YahooSeller
import jp.hibeiko.yarnshelf.data.YahooSellerReview
import jp.hibeiko.yarnshelf.data.YahooShipping
import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.hibeiko.yarnshelf.network.YahooShoppingWebServiceItemSearchApiService

class YahooShoppingWebServiceItemSearchDummyApiService : YahooShoppingWebServiceItemSearchApiService {
    override suspend fun searchItem(
        appId: String,
        janCode: String,
        query: String,
        genreCategoryId: Int,
        sort: String
    ): YahooShoppingWebServiceItemData {
        return YahooShoppingWebServiceItemDummyData.dummyData
    }
}

object YahooShoppingWebServiceItemDummyData{
    val dummyData = YahooShoppingWebServiceItemData(
        10, 0, 0, YahooRequest(""),
        hits = listOf(
            YahooHit(
                index = 0,
                name = "ダルマ毛糸（横田）",
                description = "●品質構成：ウール(ランブイエメリノウール）　60%、綿(スーピマ)　40％<br>●棒針標準ゲージ: （メリヤス編み）26〜28目・34〜37段<br>●仕立（糸長）: 50g　約166m<br>●棒針（号）: 3〜5<br>●金属かぎ針（号）: 4/0〜5/0<br>●糸使用量（平均）：帽子約1玉、ベスト約4〜5玉<br><br>※画像の関係により現物とは色が異なる場合がございます。<br>※こちらの商品はお取り寄せ商品となりますので、お届けまで4営業日間前後いただきます。<br><br>ランブイエメリノウールのスポンディッシュできめ細やかな質感と、自然な光沢が美しいスーピマコットンをブレンドした贅沢な糸です。春先から秋口までの長い季節を楽しめる糸です。コットンの持つさらっとした肌ざわりがありながら、ウールの持つ柔らかくなめらかな軽いタッチで絶妙なバランスに仕上がっています。",
                headLine = "",
                inStock = true,
                url = "",
                code = "",
                condition = "",
                premiumPrice = 0,
                premiumDiscountType = "",
                premiumDiscountRate = 0,
                imageId = "",
                image = YahooImage(
                    small = "",
                    medium = "https://item-shopping.c.yimg.jp/i/g/ko-da_dr-s-16"
                ),
                review = YahooReview(rate = 0.0, count = 0, url = ""),
                affiliateRate = 0.0,
                price = 0,
                priceLabel = YahooPriceLabel(
                    taxable = true,
                    premiumPrice = 0,
                    defaultPrice = 0,
                    discountedPrice = 0,
                    fixedPrice = 0,
                    periodStart = 0,
                    periodEnd = 0
                ),
                point = YahooPoint(
                    amount = 0,
                    times = 0,
                    bonusAmount = 0,
                    bonusTimes = 0,
                    premiumAmount = 0,
                    premiumTimes = 0,
                    premiumBonusAmount = 0,
                    premiumBonusTimes = 0
                ),
                shipping = YahooShipping(name = "", code = 0),
                genreCategory = YahooGenreCategory(id = 0, name = "", depth = 0),
                parentGenreCategories = listOf(YahooParentGenreCategories(id=0,name="", depth = 0)),
                brand = YahooBrand(id = 0, name = ""),
                parentBrands = listOf(YahooParentBrands(id = 0, name = "")),
                janCode = "",
                payment = "",
                releaseDate = "",
                seller = YahooSeller(
                    sellerId = "",
                    name = "",
                    url = "",
                    isBestSeller = true,
                    review = YahooSellerReview(rate = 0.0, count = 0),
                    imageId = ""
                ),
                delivery = YahooDelivery(area = "", deadLine = 0, day = 0),
            ),
            YahooHit(
                index = 1,
                name = "ダルマ毛糸（横田）",
                description = "強い光沢をあえて抑えたマットな質感のレース糸。スーピマ綿の中でも厳選された綿花を使用しているのでシルケット加工する必要がなく、自然な光沢が魅力です。",
                headLine = "",
                inStock = true,
                url = "",
                code = "",
                condition = "",
                premiumPrice = 0,
                premiumDiscountType = "",
                premiumDiscountRate = 0,
                imageId = "",
                image = YahooImage(
                    small = "",
                    medium = "https://item-shopping.c.yimg.jp/i/g/ko-da_dr-s-16"
                ),
                review = YahooReview(rate = 0.0, count = 0, url = ""),
                affiliateRate = 0.0,
                price = 0,
                priceLabel = YahooPriceLabel(
                    taxable = true,
                    premiumPrice = 0,
                    defaultPrice = 0,
                    discountedPrice = 0,
                    fixedPrice = 0,
                    periodStart = 0,
                    periodEnd = 0
                ),
                point = YahooPoint(
                    amount = 0,
                    times = 0,
                    bonusAmount = 0,
                    bonusTimes = 0,
                    premiumAmount = 0,
                    premiumTimes = 0,
                    premiumBonusAmount = 0,
                    premiumBonusTimes = 0
                ),
                shipping = YahooShipping(name = "", code = 0),
                genreCategory = YahooGenreCategory(id = 0, name = "", depth = 0),
                parentGenreCategories = listOf(YahooParentGenreCategories(id=0,name="", depth = 0)),
                brand = YahooBrand(id = 0, name = ""),
                parentBrands = listOf(YahooParentBrands(id = 0, name = "")),
                janCode = "",
                payment = "",
                releaseDate = "",
                seller = YahooSeller(
                    sellerId = "",
                    name = "",
                    url = "",
                    isBestSeller = true,
                    review = YahooSellerReview(rate = 0.0, count = 0),
                    imageId = ""
                ),
                delivery = YahooDelivery(area = "", deadLine = 0, day = 0),
            ),
            YahooHit(
                index = 2,
                name = "ダルマ毛糸（横田）",
                description = "強い光沢をあえて抑えたマットな質感のレース糸。スーピマ綿の中でも厳選された綿花を使用しているのでシルケット加工する必要がなく、自然な光沢が魅力です。",
                janCode = "",
                headLine = "",
                inStock = true,
                url = "",
                code = "",
                condition = "",
                premiumPrice = 0,
                premiumDiscountType = "",
                premiumDiscountRate = 0,
                imageId = "",
                image = YahooImage(
                    small = "",
                    medium = "https://item-shopping.c.yimg.jp/i/g/ko-da_dr-s-16"
                ),
                review = YahooReview(rate = 0.0, count = 0, url = ""),
                affiliateRate = 0.0,
                price = 0,
                priceLabel = YahooPriceLabel(
                    taxable = true,
                    premiumPrice = 0,
                    defaultPrice = 0,
                    discountedPrice = 0,
                    fixedPrice = 0,
                    periodStart = 0,
                    periodEnd = 0
                ),
                point = YahooPoint(
                    amount = 0,
                    times = 0,
                    bonusAmount = 0,
                    bonusTimes = 0,
                    premiumAmount = 0,
                    premiumTimes = 0,
                    premiumBonusAmount = 0,
                    premiumBonusTimes = 0
                ),
                shipping = YahooShipping(name = "", code = 0),
                genreCategory = YahooGenreCategory(id = 0, name = "", depth = 0),
                parentGenreCategories = listOf(YahooParentGenreCategories(id=0,name="", depth = 0)),
                brand = YahooBrand(id = 0, name = ""),
                parentBrands = listOf(YahooParentBrands(id = 0, name = "")),
                payment = "",
                releaseDate = "",
                seller = YahooSeller(
                    sellerId = "",
                    name = "",
                    url = "",
                    isBestSeller = true,
                    review = YahooSellerReview(rate = 0.0, count = 0),
                    imageId = ""
                ),
                delivery = YahooDelivery(area = "", deadLine = 0, day = 0),
            )
        )
    )
}