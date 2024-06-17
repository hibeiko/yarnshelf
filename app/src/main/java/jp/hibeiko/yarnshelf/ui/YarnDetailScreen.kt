package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme

object YarnDetailDestination : NavigationDestination {
    override val route = "YarnInfoDetail"
    override val title = "けいと情報"
    const val yarnIdArg = "yarnId"
    val routeWithArgs = "${route}/{$yarnIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnDetailScreen(
    // ViewModel(UiStateを使うため)
    yarnDetailScreenViewModel: YarnDetailScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: (Int) -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // UiStateを取得
    val yarnDetailScreenUiState by yarnDetailScreenViewModel.yarnDetailScreenUiState.collectAsState()

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
                            text = YarnDetailDestination.title,
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
            YarnDetailScreenBody(
                yarnDetailScreenUiState,
                nextButtonOnClick,
                cancelButtonOnClick,
                yarnDetailScreenViewModel::deleteYarnData,
                modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun YarnDetailScreenBody(
    yarnDetailScreenUiState: YarnDetailScreenUiState,
    nextButtonOnClick: (Int) -> Unit,
    cancelButtonOnClick: () -> Unit,
    deleteButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Text(
                text = "名前",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(10.dp)
            )
            Text(
                text = yarnDetailScreenUiState.yarnDetailData.yarnName,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(10.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Text(
                text = "メモ",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(10.dp)
            )
            Text(
                text = yarnDetailScreenUiState.yarnDetailData.yarnDescription,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(10.dp)
                    .height(150.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (yarnDetailScreenUiState.yarnDetailData.imageUrl) {
                "" ->
                    Image(
                        painter = painterResource(
                            when (yarnDetailScreenUiState.yarnDetailData.drawableResourceId) {
                                0 -> R.drawable.not_found
                                else -> yarnDetailScreenUiState.yarnDetailData.drawableResourceId
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .width(140.dp),
                    )

                else -> AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(yarnDetailScreenUiState.yarnDetailData.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(140.dp)
                        .width(140.dp),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1.0F))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = cancelButtonOnClick
            ) {
                Text(text = "Cancel", style = MaterialTheme.typography.labelSmall)
            }
            Button(
                onClick = {
                    deleteButtonOnClick()
                    cancelButtonOnClick()
                },
            ) {
                Text(text = "削除", style = MaterialTheme.typography.labelSmall)
            }
            Button(onClick = { nextButtonOnClick(yarnDetailScreenUiState.yarnDetailData.yarnId) }) {
                Text(text = "編集", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview
@Composable
fun YarnDetailScreenPreview() {
    YarnShelfTheme {
        YarnDetailScreenBody(
            YarnDetailScreenUiState(yarnDetailData = YarnData()),
            nextButtonOnClick = {},
            cancelButtonOnClick = {},
            deleteButtonOnClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}