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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    override val title = "けいとの詳細"
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
    val dialogViewFlag by yarnDetailScreenViewModel.dialogViewFlag.collectAsState()

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
            }
        ) { innerPadding ->
            Column(modifier = modifier.padding(innerPadding)) {
                YarnDetailScreenBody(
                    modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1.0f, false),
                    yarnDetailScreenUiState.yarnDetailData,
                    dialogViewFlag,
                    yarnDetailScreenViewModel::updateDialogViewFlag,
                    yarnDetailScreenViewModel::deleteYarnData,
                    cancelButtonOnClick,
                )
                YarnDetailScreenBottom(
                    modifier,
                    yarnDetailScreenUiState.yarnDetailData,
                    nextButtonOnClick,
                    yarnDetailScreenViewModel::updateDialogViewFlag,
                    cancelButtonOnClick,
                )
            }
        }
    }
}

@Composable
fun YarnDetailScreenBody(
    modifier: Modifier = Modifier,
    yarnData: YarnData,
    dialogViewFlag: Boolean,
    updateDialogViewFlag: (Boolean) -> Unit,
    deleteYarnData: () -> Unit,
    cancelButtonOnClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            if (yarnData.yarnMakerName.isNotBlank()) {
                Text(
                    text = yarnData.yarnMakerName,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 0.dp, bottom = 4.dp, start = 12.dp)
                )
            }
            Text(
                text = yarnData.yarnName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 0.dp, bottom = 4.dp, start = 12.dp)
            )
            when (yarnData.imageUrl) {
                "" ->
                    Image(
                        painter = painterResource(R.drawable.not_found),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 12.dp)
                            .height(240.dp)
                            .width(240.dp)
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
                        .padding(top = 4.dp, bottom = 12.dp)
                        .height(240.dp)
                        .width(240.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "残量：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = "${yarnData.havingNumber}玉",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 12.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "品質：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = yarnData.quality.ifBlank { "-" },
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = "標準状態重量：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = formatWeightStringForScreen(
                    yarnData.weight,
                    yarnData.length,
                    yarnData.roll
                ),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = "糸の太さ：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = yarnData.thickness.value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 12.dp)
            )
            Text(
                text = "色番号：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = yarnData.colorNumber,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 12.dp)
            )
            Text(
                text = "ロット番号：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = yarnData.rotNumber,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 12.dp)
            )
            Text(
                text = "標準ゲージ：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = formatGaugeStringForScreen(
                    yarnData.gaugeColumnFrom,
                    yarnData.gaugeColumnTo,
                    yarnData.gaugeRowFrom,
                    yarnData.gaugeRowTo,
                    yarnData.gaugeStitch
                ),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = "参考使用針：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 10.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = formatNeedleSizeStringForScreen(
                    yarnData.needleSizeFrom,
                    yarnData.needleSizeTo,
                    yarnData.crochetNeedleSizeFrom,
                    yarnData.crochetNeedleSizeTo
                ),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 12.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "JANコード：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = yarnData.janCode,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 12.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "メモ：",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            if (yarnData.yarnDescription.isNotBlank()) {
                Text(
                    text = yarnData.yarnDescription,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 5,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 12.dp, bottom = 12.dp, end = 12.dp)
//                        .verticalScroll(rememberScrollState())
                )
            }

        }
//        Spacer(modifier = Modifier.weight(1.0F))
    }
    if (dialogViewFlag) {
        ConfirmationDialog(
            onDismissRequest = updateDialogViewFlag,
            onConfirmation = {
                deleteYarnData()
                cancelButtonOnClick()
            },
            titleText = "確認",
            dialogText = "削除したデータは元に戻せません。削除してよろしいですか？",
            confirmButtonText = stringResource(R.string.delete)
        )

    }
}

@Composable
fun ConfirmationDialog(
    onDismissRequest: (Boolean) -> Unit,
    onConfirmation: () -> Unit,
    titleText : String,
    dialogText: String,
    confirmButtonText: String,
){
    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Confirmation")
        },
        title = {
            Text(text = titleText)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {onDismissRequest(false)} ,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = {onDismissRequest(false)}) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
@Composable
fun YarnDetailScreenBottom(
    modifier: Modifier,
    yarnData: YarnData,
    nextButtonOnClick: (Int) -> Unit,
    updateDialogViewFlag: (Boolean) -> Unit,
    cancelButtonOnClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            onClick = cancelButtonOnClick
        ) {
            Text(text = stringResource(R.string.back), style = MaterialTheme.typography.labelSmall)
        }
        Button(
            onClick = {updateDialogViewFlag(true)},
        ) {
            Text(text = stringResource(R.string.delete), style = MaterialTheme.typography.labelSmall)
        }
        Button(onClick = { nextButtonOnClick(yarnData.yarnId) }) {
            Text(text = stringResource(R.string.edit), style = MaterialTheme.typography.labelSmall)
        }
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
                "https://image.raku-uru.jp/01/19110/456/Spin+1010+Crpd_1625196651766.JPG",
            ),
            dialogViewFlag = false,
            updateDialogViewFlag = {_ -> },
            deleteYarnData = {},
            cancelButtonOnClick = {}
        )
    }
}
@Preview
@Composable
fun YarnDetailBottomPreview() {
    YarnShelfTheme {
        YarnDetailScreenBottom(
            modifier = Modifier
                .fillMaxWidth()
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
                "https://image.raku-uru.jp/01/19110/456/Spin+1010+Crpd_1625196651766.JPG",
            ),
            nextButtonOnClick = { _ -> },
            updateDialogViewFlag = {_ ->},
            cancelButtonOnClick = {}
        )
    }
}