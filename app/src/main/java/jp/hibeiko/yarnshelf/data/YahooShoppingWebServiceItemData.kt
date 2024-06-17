package jp.hibeiko.yarnshelf.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YahooShoppingWebServiceItemData(
    val totalResultsAvailable: Int,
    val totalResultsReturned: Int,
    val firstResultsPosition: Int,
    val hits: List<YahooHit>,
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
//    val taxExcludePrice: Int?,
    val price: Int?,
    val imageId: String?,
    val janCode: String?,
    val image: YahooImage?,
    val brand: YahooBrand?
)
@Serializable
@SerialName("image")
data class YahooImage(
    val small: String?,
    val medium: String?,

)

@Serializable
@SerialName("brand")
data class YahooBrand(
    val id: Int?,
    val name: String?,
)