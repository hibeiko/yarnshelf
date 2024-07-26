package jp.hibeiko.yarnshelf.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YahooShoppingWebServiceItemData(
    val totalResultsAvailable: Int,
    val totalResultsReturned: Int,
    val firstResultsPosition: Int,
    val request: YahooRequest,
    val hits: List<YahooHit>,
)

@Serializable
@SerialName("request")
data class YahooRequest(
    val query: String?
)
@Serializable
@SerialName("hits")
data class YahooHit(
    val index: Int,
    val name: String?,
    val description: String?,
    val headLine: String?,
    val inStock: Boolean?,
    val url: String?,
    val code: String?,
    val condition: String?,
//    val taxExcludePrice: Int?,
//    val taxExcludePremiumPrice: Int?,
    val premiumPrice: Int?,
    val premiumDiscountType: String?,
    val premiumDiscountRate: Int?,
    val imageId: String?,
    val image: YahooImage,
//    val exImage: YahooExImage,
    val review: YahooReview,
    val affiliateRate: Double?,
    val price: Int,
    val priceLabel: YahooPriceLabel,
    val point: YahooPoint,
    val shipping: YahooShipping,
    val genreCategory: YahooGenreCategory,
    val parentGenreCategories: List<YahooParentGenreCategories>,
    val brand: YahooBrand,
    val parentBrands: List<YahooParentBrands>,
    val janCode: String?,
    val payment: String?,
    val releaseDate: Int?,
    val seller: YahooSeller,
    val delivery: YahooDelivery,
)
@Serializable
@SerialName("image")
data class YahooImage(
    val small: String?,
    val medium: String?,
)
//@Serializable
//@SerialName("exImage")
//data class YahooExImage(
//    val url: String?,
//    val width: Int?,
//    val height: Int?,
//)
@Serializable
@SerialName("review")
data class YahooReview(
    val rate: Double?,
    val count: Int?,
    val url: String?,
)
@Serializable
@SerialName("priceLabel")
data class YahooPriceLabel(
    val taxable: Boolean?,
    val premiumPrice: Int?,
//    val taxExcludePremiumPrice: Int?,
    val defaultPrice: Int?,
//    val taxExcludeDefaultPrice: Int?,
    val discountedPrice: Int?,
//    val taxExcludeDiscountedPrice: Int?,
    val fixedPrice: Int?,
    val periodStart: Int?,
    val periodEnd: Int?,
)
@Serializable
@SerialName("point")
data class YahooPoint(
    val amount: Int?,
    val times: Int?,
    val bonusAmount: Int?,
    val bonusTimes: Int?,
    val premiumAmount: Int?,
    val premiumTimes: Int?,
    val premiumBonusAmount: Int?,
    val premiumBonusTimes: Int?,
)

@Serializable
@SerialName("shipping")
data class YahooShipping(
    val name: String?,
    val code: Int?,
)
@Serializable
@SerialName("genreCategory")
data class YahooGenreCategory(
    val id: Int?,
    val name: String?,
    val depth: Int?,
)
@Serializable
@SerialName("parentGenreCategories")
data class YahooParentGenreCategories(
    val id: Int?,
    val name: String?,
    val depth: Int?,
)

@Serializable
@SerialName("brand")
data class YahooBrand(
    val id: Int?,
    val name: String?,
)
@Serializable
@SerialName("parentBrands")
data class YahooParentBrands(
    val id: Int?,
    val name: String?,
)
@Serializable
@SerialName("seller")
data class YahooSeller(
    val sellerId: String?,
    val name: String?,
    val url: String?,
    val isBestSeller: Boolean?,
    val review: YahooSellerReview,
    val imageId: String?,
)

@Serializable
@SerialName("review")
data class YahooSellerReview(
    val rate: Double?,
    val count: Int?,
)

@Serializable
@SerialName("delivery")
data class YahooDelivery(
    val area: String?,
    val deadLine: Int?,
    val day: Int?,
)

