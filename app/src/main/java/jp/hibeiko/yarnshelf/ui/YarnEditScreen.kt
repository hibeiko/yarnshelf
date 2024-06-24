package jp.hibeiko.yarnshelf.ui

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import jp.hibeiko.yarnshelf.common.YarnParamName
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import jp.hibeiko.yarnshelf.common.yarnDataToYarnDataForScreenConverter
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme
import java.text.DecimalFormat
import java.util.Date

object YarnEditDestination : NavigationDestination {
    override val route = "YarnInfoEdit"
    override val title = "毛糸情報編集画面"
    const val yarnIdArg = "yarnId"
    val routeWithArgs = "${route}/{$yarnIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnEditScreen(
    modifier: Modifier = Modifier,
    // ViewModel(UiStateを使うため)
    yarnEditScreenViewModel: YarnEditScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: (YarnDataForScreen) -> Unit,
    cancelButtonOnClick: () -> Unit,
) {
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
                            text = YarnEditDestination.title,
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
                                nextButtonOnClick(
                                    yarnDataToYarnDataForScreenConverter(
                                        yarnEditScreenViewModel.yarnEditScreenUiState.yarnEditData
                                    )
                                )
                            },
                            enabled = yarnEditScreenViewModel.validateInput()
                        ) {
                            Text(text = "Next", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        ) { innerPadding ->
            YarnEditScreenBody(
                yarnEditScreenViewModel.yarnEditScreenUiState.yarnEditData,
                yarnEditScreenViewModel::updateYarnEditData,
                modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
fun YarnEditScreenBody(
    yarnData: YarnData,
    updateYarnEditData: (Any, YarnParamName) -> Unit,
    modifier: Modifier = Modifier
) {
//    Log.d("YarnEditScreen", "$yarnData")

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                label = { Text("メーカー", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.yarnMakerName,
                onValueChange = { updateYarnEditData(it, YarnParamName.YARN_MAKER_NAME) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp)
            )
            TextField(
                label = { Text("名前", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.yarnName,
                onValueChange = { updateYarnEditData(it, YarnParamName.YARN_NAME) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
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
            TextField(
                label = { Text("残量(単位：玉)", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = DecimalFormat("#.#").format(yarnData.havingNumber),
                onValueChange = { updateYarnEditData(it, YarnParamName.HAVING_NUMBER) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                label = { Text("品質", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.quality,
                onValueChange = { updateYarnEditData(it, YarnParamName.QUALITY) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp, start = 10.dp)
            )
            TextField(
                label = { Text("重量(単位：g)", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = if (yarnData.weight != null) DecimalFormat("#.#").format(yarnData.weight) else "",
                onValueChange = { updateYarnEditData(it, YarnParamName.WEIGHT) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
            )
            TextField(
                label = { Text("糸長(単位：m)", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = if (yarnData.length != null) DecimalFormat("#.#").format(yarnData.length) else "",
                onValueChange = { updateYarnEditData(it, YarnParamName.LENGTH) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
            )
            Text(
                text = "糸の太さ：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = if (yarnData.thickness.value == "") "未設定" else yarnData.thickness.value,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Slider(
                value = yarnData.thickness.rowValue.toFloat(),
                onValueChange = {
                    updateYarnEditData(
                        YarnThickness.entries.first { yarnThicknessItem -> Math.round(it).toInt() == yarnThicknessItem.rowValue },
                        YarnParamName.THICKNESS
                    )
                },
                steps = YarnThickness.entries.size - 2,
                valueRange = 0f..(YarnThickness.entries.size-1).toFloat(),
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            TextField(
                label = { Text("色番号", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.colorNumber,
                onValueChange = { updateYarnEditData(it, YarnParamName.COLOR_NUMBER) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            TextField(
                label = { Text("ロット番号", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.rotNumber,
                onValueChange = { updateYarnEditData(it, YarnParamName.ROT_NUMBER) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = "ゲージ",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
//                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.gaugeColumnFrom != null) DecimalFormat("#.#").format(
                        yarnData.gaugeColumnFrom
                    ) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_COLUMN_FROM) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
                TextField(
//                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
//                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.gaugeColumnTo != null) DecimalFormat("#.#").format(yarnData.gaugeColumnTo) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_COLUMN_TO) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    enabled = yarnData.gaugeColumnFrom != null,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
                Text(
                    text = "目",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    //                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.gaugeRowFrom != null) DecimalFormat("#.#").format(yarnData.gaugeRowFrom) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_ROW_FROM) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
                TextField(
                    //                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
                    //                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.gaugeRowTo != null) DecimalFormat("#.#").format(yarnData.gaugeRowTo) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_ROW_TO) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    enabled = yarnData.gaugeRowFrom != null,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
                Text(
                    text = "段",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
            }
            TextField(
                label = {
                    Text(
                        "ゲージの編み方(メリヤス編み、模様編みなど)",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.gaugeStitch,
                onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_STITCH) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, bottom = 10.dp, start = 10.dp)
            )
            Text(
                text = "参考使用針",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "棒針",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
                TextField(
//                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.needleSizeFrom != null) DecimalFormat("#.#").format(
                        yarnData.needleSizeFrom
                    ) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.NEEDLE_SIZE_FROM) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
                TextField(
//                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
//                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.needleSizeTo != null) DecimalFormat("#.#").format(yarnData.needleSizeTo) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.NEEDLE_SIZE_TO) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    enabled = yarnData.needleSizeFrom != null,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "かぎ針",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
                TextField(
                    //                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.crochetNeedleSizeFrom != null) DecimalFormat("#.#").format(
                        yarnData.crochetNeedleSizeFrom
                    ) else "",
                    onValueChange = {
                        updateYarnEditData(
                            it,
                            YarnParamName.CROCHET_NEEDLE_SIZE_FROM
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
                TextField(
                    //                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
                    //                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    value = if (yarnData.crochetNeedleSizeTo != null) DecimalFormat("#.#").format(
                        yarnData.crochetNeedleSizeTo
                    ) else "",
                    onValueChange = {
                        updateYarnEditData(
                            it,
                            YarnParamName.CROCHET_NEEDLE_SIZE_TO
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    enabled = yarnData.crochetNeedleSizeFrom != null,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 5.dp, start = 10.dp)
                        .width(100.dp)
                )
            }
            TextField(
                label = { Text("メモ", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.yarnDescription,
                onValueChange = { updateYarnEditData(it, YarnParamName.YARN_DESCRIPTION) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                textStyle = MaterialTheme.typography.displayMedium,
                singleLine = false,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
//        Spacer(modifier = Modifier.weight(1.0F))
    }
}

@Preview
@Composable
fun YarnEditScreenPreview() {
    YarnShelfTheme {
        YarnEditScreenBody(
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
            updateYarnEditData = { _, _ -> },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}