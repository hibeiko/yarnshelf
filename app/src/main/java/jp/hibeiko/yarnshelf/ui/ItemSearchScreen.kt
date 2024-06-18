package jp.hibeiko.yarnshelf.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.data.YahooHit
import jp.hibeiko.yarnshelf.data.YahooImage
import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.hibeiko.yarnshelf.data.YarnData
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
    // ViewModel(UiStateを使うため)
    itemSearchScreenViewModel: ItemSearchScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: (YarnDataForScreen) -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
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
                        IconButton(onClick = cancelButtonOnClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "戻る",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
            ItemSearchScreenBody(
                itemSearchScreenUiState,
                itemSearchScreenViewModel.searchItemUiState,
                itemSearchScreenViewModel::yarnNameSearchInputUpdate,
                itemSearchScreenViewModel::yarnJanCodeSearchInputUpdate,
                itemSearchScreenViewModel::validateYarnNameSearchInput,
                itemSearchScreenViewModel::validateJanCodeSearchInput,
                itemSearchScreenViewModel::searchItem,
                nextButtonOnClick,
                modifier
                    .padding(innerPadding)
//                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
fun ItemSearchScreenBody(
    itemSearchScreenUiState: ItemSearchScreenUiState,
    searchItemUiState: SearchItemUiState,
    yarnNameSearchInputUpdate: (String) -> Unit,
    yarnJanCodeSearchInputUpdate: (String) -> Unit,
    validateYarnNameSearchInput: () -> Boolean,
    validateJanCodeSearchInput: () -> Boolean,
    searchItemOnClick: (String, String) -> Unit,
    nextButtonOnClick: (YarnDataForScreen) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
    ) {
        TextField(
            label = { Text("JANコード", style = MaterialTheme.typography.labelSmall) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
            value = itemSearchScreenUiState.yarnJanCodeSearchInput,
            onValueChange = { yarnJanCodeSearchInputUpdate(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium
        )
        Button(
            onClick = {
                searchItemOnClick(
                    "",
                    itemSearchScreenUiState.yarnJanCodeSearchInput
                )
            },
            enabled = validateJanCodeSearchInput()
        ) {
            Text(text = "JANコードから検索する", style = MaterialTheme.typography.labelSmall)
        }
        TextField(
            label = { Text("商品名", style = MaterialTheme.typography.labelSmall) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
            value = itemSearchScreenUiState.yarnNameSearchInput,
            onValueChange = { yarnNameSearchInputUpdate(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
        Button(
            onClick = {
                searchItemOnClick(
                    itemSearchScreenUiState.yarnNameSearchInput,
                    ""
                )
            },
            enabled = validateYarnNameSearchInput()
        ) {
            Text(text = "商品名から検索する", style = MaterialTheme.typography.labelSmall)
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
                LazyRow {
                    items(searchItemUiState.responseItem.hits) {
                        SearchResultCard(
                            it,
                            cardOnClick = nextButtonOnClick ,
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
//        Spacer(modifier = Modifier.weight(1.0F))
        Button(
            onClick = {
                nextButtonOnClick(YarnDataForScreen())
            },
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(text = "手入力で登録する", style = MaterialTheme.typography.labelSmall)
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
            .height(480.dp)
            .width(250.dp)
            .padding(top = 10.dp, bottom = 10.dp, start = 5.dp, end = 5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = {
            Log.d("ItemSearchScreen","$hitItem")
            val temp =                 YarnDataForScreen(
                yarnName = hitItem.name ?: "",
                yarnDescription = hitItem.description ?: "",
                imageUrl = hitItem.image?.medium ?: "",
                janCode = hitItem.janCode ?: ""
            )
            cardOnClick(temp)
            Log.d("ItemSearchScreen","$temp")
                  },
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(hitItem.image?.medium)
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
            itemSearchScreenUiState = ItemSearchScreenUiState(itemSearchData = YarnData()),
            searchItemUiState = SearchItemUiState.Success(
                responseItem = YahooShoppingWebServiceItemData(
                    10, 0, 0, hits = listOf(
                        YahooHit(
                            index = 0,
                            name = "ダルマ毛糸（横田）",
                            description = "強い光沢をあえて抑えたマットな質感のレース糸。スーピマ綿の中でも厳選された綿花を使用しているのでシルケット加工する必要がなく、自然な光沢が魅力です。",
                            janCode = "",
                            image = YahooImage(
                                small = "",
                                medium = "https://item-shopping.c.yimg.jp/i/g/ko-da_dr-s-16"
                            )
                        ),
                        YahooHit(
                            index = 1,
                            name = "ダルマ毛糸（横田）",
                            description = "強い光沢をあえて抑えたマットな質感のレース糸。スーピマ綿の中でも厳選された綿花を使用しているのでシルケット加工する必要がなく、自然な光沢が魅力です。",
                            janCode = "",
                            image = YahooImage(
                                small = "",
                                medium = "https://item-shopping.c.yimg.jp/i/g/ko-da_dr-s-16"
                            )
                        ),
                        YahooHit(
                            index = 2,
                            name = "ダルマ毛糸（横田）",
                            description = "強い光沢をあえて抑えたマットな質感のレース糸。スーピマ綿の中でも厳選された綿花を使用しているのでシルケット加工する必要がなく、自然な光沢が魅力です。",
                            janCode = "",
                            image = YahooImage(
                                small = "",
                                medium = "https://item-shopping.c.yimg.jp/i/g/ko-da_dr-s-16"
                            )
                        )
                    )
                )
            ),
            yarnNameSearchInputUpdate = {},
            yarnJanCodeSearchInputUpdate = {},
            validateYarnNameSearchInput = { false },
            validateJanCodeSearchInput = { true },
            searchItemOnClick = { _, _ -> },
            nextButtonOnClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}