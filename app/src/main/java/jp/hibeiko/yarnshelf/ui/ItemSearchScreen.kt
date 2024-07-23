package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.common.yahooHitToYarnDataForScreenConverter
import jp.hibeiko.yarnshelf.data.YahooBrand
import jp.hibeiko.yarnshelf.data.YahooDelivery
import jp.hibeiko.yarnshelf.data.YahooGenreCategory
import jp.hibeiko.yarnshelf.data.YahooHit
import jp.hibeiko.yarnshelf.data.YahooImage
import jp.hibeiko.yarnshelf.data.YahooPoint
import jp.hibeiko.yarnshelf.data.YahooPriceLabel
import jp.hibeiko.yarnshelf.data.YahooRequest
import jp.hibeiko.yarnshelf.data.YahooReview
import jp.hibeiko.yarnshelf.data.YahooSeller
import jp.hibeiko.yarnshelf.data.YahooSellerReview
import jp.hibeiko.yarnshelf.data.YahooShipping
import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme

object ItemSearchDestination : NavigationDestination {
    override val route = "ItemSearch"
    override val title = "あたらしい毛糸を登録"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchScreen(
    modifier: Modifier = Modifier,
    // ViewModel(UiStateを使うため)
    itemSearchScreenViewModel: ItemSearchScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
) {
    // UiStateを取得
    val itemSearchScreenUiState by itemSearchScreenViewModel.itemSearchScreenUiState.collectAsState()

    // 画面トップ
    Surface(
        modifier = modifier
    ) {
        Scaffold(
            // トップバー
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            text = ItemSearchDestination.title,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (itemSearchScreenUiState.entryScreenViewFlag) itemSearchScreenViewModel.backToItemSearchScreen()
                                else cancelButtonOnClick()
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
            if (itemSearchScreenUiState.entryScreenViewFlag) {
                Column(modifier = modifier.padding(innerPadding)) {
                    YarnEditScreenBody(
                        yarnData = itemSearchScreenUiState.itemSearchData,
                        updateYarnEditData = itemSearchScreenViewModel::updateYarnEditData,
                        isErrorMap = itemSearchScreenUiState.isErrorMap,
                        modifier
                            .verticalScroll(rememberScrollState())
                            .weight(1.0f, false)
                    )
                    YarnEditScreenBottom(
                        isErrorMap = itemSearchScreenUiState.isErrorMap,
                        itemSearchScreenViewModel::updateDialogViewFlag,
                        itemSearchScreenViewModel::backToItemSearchScreen,
                        modifier
                    )
                }
                if (itemSearchScreenUiState.confirmDialogViewFlag) {
                    ConfirmationDialog(
                        onDismissRequest = itemSearchScreenViewModel::updateDialogViewFlag,
                        onConfirmation = {
                            itemSearchScreenViewModel.updateYarnData()
                            nextButtonOnClick()
                        },
                        titleText = "確認",
                        dialogText = "この内容で登録します。よろしいですか？",
                        confirmButtonText = stringResource(R.string.ok)
                    )

                }
            } else {
                ItemSearchScreenBody(
                    itemSearchScreenUiState,
                    itemSearchScreenViewModel.searchItemUiState,
                    itemSearchScreenViewModel::yarnNameSearchInputUpdate,
                    itemSearchScreenViewModel::selectedTabIndexUpdate,
                    itemSearchScreenViewModel::validateYarnNameSearchInput,
                    itemSearchScreenViewModel::searchItem,
                    itemSearchScreenViewModel::readBarcode,
                    itemSearchScreenViewModel::navigateToYarnEntryScreen,
                    modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
fun ItemSearchScreenBody(
    itemSearchScreenUiState: ItemSearchScreenUiState,
    searchItemUiState: SearchItemUiState,
    yarnNameSearchInputUpdate: (String) -> Unit,
    selectedTabIndexUpdate: (Int) -> Unit,
    validateYarnNameSearchInput: () -> Boolean,
    searchItemOnClick: (String, String) -> Unit,
    readBarcode: () -> Unit,
    cardOnClick: (YarnDataForScreen) -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        TabRow(
            selectedTabIndex = itemSearchScreenUiState.selectedTabIndex
        ) {
            Tab(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.outline_barcode_24),
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(R.string.itemsearchscreen_barcode_tab_name),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                selected = itemSearchScreenUiState.selectedTabIndex == 0,
                onClick = { selectedTabIndexUpdate(0) }
            )
            Tab(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Text(
                            text = stringResource(R.string.itemsearchscreen_itemsearch_tab_name),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                selected = itemSearchScreenUiState.selectedTabIndex == 0,
                onClick = { selectedTabIndexUpdate(1) }
            )
            Tab(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Text(
                            text = stringResource(R.string.itemsearchscreen_manualinput_tab_name),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                selected = itemSearchScreenUiState.selectedTabIndex == 0,
                onClick = { cardOnClick(YarnDataForScreen()) }
            )
        }
//        Button(
//            onClick = {
//                nextButtonOnClick(YarnDataForScreen())
//            },
//            modifier = Modifier.padding(top = 10.dp)
//        ) {
//            Text(text = "手入力で登録する", style = MaterialTheme.typography.labelSmall)
//        }
        when (itemSearchScreenUiState.selectedTabIndex) {
            0 -> {
                // バーコードスキャン
//                Button(
//                    onClick = { readBarcode() },
//                ) {
//                    Text(text = "カメラ起動", style = MaterialTheme.typography.labelSmall)
//                }
//                TextField(
//                    label = { Text("JANコード", style = MaterialTheme.typography.labelSmall) },
//                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
//                    trailingIcon = {
//                        IconButton(
//                            onClick = {yarnJanCodeSearchInputUpdate("")}
//                        ) {
//                            Icon(Icons.Default.Clear, contentDescription = null)
//                        }
//                    },
//                    value = itemSearchScreenUiState.yarnJanCodeSearchInput,
//                    onValueChange = { yarnJanCodeSearchInputUpdate(it) },
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Done
//                    ),
//                    singleLine = true,
//                    textStyle = MaterialTheme.typography.displayMedium
//                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { readBarcode() },
//                    enabled = validateJanCodeSearchInput()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_photo_camera_24),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.itemsearchscreen_scan_button_name),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            1 -> {
                // 検索
                TextField(
                    label = {
                        Text(
                            stringResource(R.string.itemsearchscreen_input_search_text),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    trailingIcon = {
                        IconButton(
//                    onClick = {yarnNameSearchInputUpdate("")}
                            onClick = {
                                if (validateYarnNameSearchInput())
                                    searchItemOnClick(
                                        itemSearchScreenUiState.yarnNameSearchInput,
                                        ""
                                    )
                            }
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = stringResource(R.string.itemsearchscreen_itemsearch_tab_name)
                            )
                        }
                    },
                    value = itemSearchScreenUiState.yarnNameSearchInput,
                    onValueChange = { yarnNameSearchInputUpdate(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )
//        Button(
//            onClick = {
//                searchItemOnClick(
//                    itemSearchScreenUiState.yarnNameSearchInput,
//                    ""
//                )
//            },
//            enabled = validateYarnNameSearchInput()
//        ) {
//            Text(text = "商品名から検索する", style = MaterialTheme.typography.labelSmall)
//        }
            }

            else -> {}
        }
        when (searchItemUiState) {
            is SearchItemUiState.Success -> {
                Text(
                    text = "検索結果：${searchItemUiState.responseItem.totalResultsAvailable}件",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "登録したい商品を選択してください。",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 10.dp)
                )
                LazyRow() {
                    items(searchItemUiState.responseItem.hits) {
                        SearchResultCard(
                            it,
                            cardOnClick = cardOnClick,
//                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }

            is SearchItemUiState.Error ->
                Text(
                    text = "商品検索に失敗しました。インターネット接続を確認してください。",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 10.dp)
                )

            is SearchItemUiState.Loading ->
                Image(
                    painter = painterResource(R.drawable.loading_img),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
        }
    }
}

@Composable
fun SearchResultCard(
    hitItem: YahooHit,
    cardOnClick: (YarnDataForScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
            .padding(top = 10.dp, bottom = 10.dp, start = 5.dp, end = 5.dp)
            .testTag("ItemSearchScreen_SearchResultCard"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = {
            cardOnClick(yahooHitToYarnDataForScreenConverter(yahooHit = hitItem))
        },
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(hitItem.image.medium)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.not_found),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp),
            )
            Text(
                text = hitItem.name ?: "",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displayMedium,
                maxLines = 2,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = hitItem.description ?: "",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 8,
                modifier = Modifier
                    .padding(top = 5.dp)
//                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Preview
@Composable
fun ItemSearchScreenPreview() {
    YarnShelfTheme {
        ItemSearchScreenBody(
            itemSearchScreenUiState = ItemSearchScreenUiState(itemSearchData = YarnDataForScreen()),
            searchItemUiState = SearchItemUiState.Success(
                responseItem = YahooShoppingWebServiceItemData(
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
                            parentGenreCategories = listOf(),
                            brand = YahooBrand(id = 0, name = ""),
                            parentBrands = listOf(),
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
                            parentGenreCategories = listOf(),
                            brand = YahooBrand(id = 0, name = ""),
                            parentBrands = listOf(),
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
                            parentGenreCategories = listOf(),
                            brand = YahooBrand(id = 0, name = ""),
                            parentBrands = listOf(),
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
            ),
            yarnNameSearchInputUpdate = {},
            selectedTabIndexUpdate = { _ -> },
            validateYarnNameSearchInput = { false },
            searchItemOnClick = { _, _ -> },
            readBarcode = {},
            cardOnClick = { _ -> },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}