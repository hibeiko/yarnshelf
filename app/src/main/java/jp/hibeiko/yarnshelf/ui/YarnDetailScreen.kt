package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
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
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import jp.hibeiko.yarnshelf.common.formatGaugeStringForScreen
import jp.hibeiko.yarnshelf.common.formatNeedleSizeStringForScreen
import jp.hibeiko.yarnshelf.common.formatWeightStringForScreen
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme
import java.util.Date

object YarnDetailDestination : NavigationDestination {
    override val route = "YarnInfoDetail"
    override val title = "けいと情報"
    const val yarnIdArg = "yarnId"
    val routeWithArgs = "${route}/{$yarnIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnDetailScreen(
    modifier: Modifier = Modifier,
    // ViewModel(UiStateを使うため)
    yarnDetailScreenViewModel: YarnDetailScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: (Int) -> Unit,
    cancelButtonOnClick: () -> Unit,
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
            // ボトムバー
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
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
                                yarnDetailScreenViewModel.deleteYarnData()
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
        ) { innerPadding ->
            YarnDetailScreenBody(
                yarnDetailScreenUiState.yarnDetailData,
                modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
fun YarnDetailScreenBody(
    modifier: Modifier = Modifier,
    yarnData: YarnData,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            if (yarnData.yarnMakerName.isNotBlank()) {
                Text(
                    text = yarnData.yarnMakerName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 5.dp, bottom = 2.dp, start = 10.dp)
                )
            }
            Text(
                text = yarnData.yarnName,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 10.dp)
            )
            when (yarnData.imageUrl) {
                "" ->
                    Image(
                        painter = painterResource(
                            when (yarnData.drawableResourceId) {
                                0 -> R.drawable.not_found
                                else -> yarnData.drawableResourceId
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 10.dp)
                            .height(140.dp)
                            .width(140.dp)
                            .align(Alignment.CenterHorizontally),
                    )

                else -> AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(yarnData.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 10.dp)
                        .height(140.dp)
                        .width(140.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "残量：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = "${yarnData.havingNumber}玉",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 10.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "品質：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = yarnData.quality.ifBlank { "-" },
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = "標準状態重量：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = formatWeightStringForScreen(
                    yarnData.weight,
                    yarnData.length,
                    yarnData.roll
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = "標準ゲージ：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = formatGaugeStringForScreen(
                    yarnData.gaugeColumnFrom,
                    yarnData.gaugeColumnTo,
                    yarnData.gaugeRowFrom,
                    yarnData.gaugeRowTo,
                    yarnData.gaugeStitch
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = "参考使用針：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = formatNeedleSizeStringForScreen(
                    yarnData.needleSizeFrom,
                    yarnData.needleSizeTo,
                    yarnData.crochetNeedleSizeFrom,
                    yarnData.crochetNeedleSizeTo
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 10.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "メモ：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, bottom = 5.dp)
            )
            if (yarnData.yarnDescription.isNotBlank()) {
                Text(
                    text = yarnData.yarnDescription,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 5,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 10.dp, end = 10.dp)
//                        .verticalScroll(rememberScrollState())
                )
            }

        }
//        Spacer(modifier = Modifier.weight(1.0F))
    }
}

@Preview
@Composable
fun YarnDetailScreenPreview() {
    YarnShelfTheme {
        YarnDetailScreenBody(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp),
            YarnData(
                0,
                "10001",
                "1010 Seabright",
                "Jamieson's",
                "1010 Seabright",
                "1548",
                "シェットランドウール１００％",
                25.01,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                10,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_1010_crpd_1625196651766_400
            ),
        )
    }
}