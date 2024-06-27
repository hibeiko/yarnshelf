package jp.hibeiko.yarnshelf.ui

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
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
import jp.hibeiko.yarnshelf.common.validateInput
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
            }
        ) { innerPadding ->
            Column(modifier = modifier.padding(innerPadding)) {
                YarnEditScreenBody(
                    yarnEditScreenViewModel.yarnEditScreenUiState.yarnEditData,
                    yarnEditScreenViewModel::updateYarnEditData,
                    yarnEditScreenViewModel::validateInput,
                    modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1.0f, false)
                )
                YarnEditScreenBottom(
                    yarnEditScreenViewModel.yarnEditScreenUiState,
                    nextButtonOnClick,
                    cancelButtonOnClick,
                    modifier
                )
            }
        }
    }
}
@Composable
fun YarnEditScreenBody(
    yarnData: YarnData,
    updateYarnEditData: (Any, YarnParamName) -> Unit,
    validateInput: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
//    Log.d("YarnEditScreen", "$yarnData")
    // バリデーションチェック実施。(trueはエラーあり falseはエラーなし)
    val yarnMakerNameIsErrorFlg =
        !validateInput(yarnData.yarnMakerName, YarnParamName.YARN_MAKER_NAME)
    val yarnNameIsErrorFlg = !validateInput(yarnData.yarnName, YarnParamName.YARN_NAME)
    val havingNumberIsErrorFlg = !validateInput(yarnData.havingNumber, YarnParamName.HAVING_NUMBER)
    val qualityIsErrorFlg = !validateInput(yarnData.quality, YarnParamName.QUALITY)
    val weightIsErrorFlg = if (yarnData.weight != null) !validateInput(
        yarnData.weight,
        YarnParamName.WEIGHT
    ) else false
    val lengthIsErrorFlg = if (yarnData.length != null) !validateInput(
        yarnData.length,
        YarnParamName.LENGTH
    ) else false
    val colorNumberIsErrorFlg =
        !validateInput(yarnData.colorNumber, YarnParamName.COLOR_NUMBER)
    val rotNumberIsErrorFlg = !validateInput(yarnData.rotNumber, YarnParamName.ROT_NUMBER)
    val gaugeColumnFromIsErrorFlg = if (yarnData.gaugeColumnFrom != null) !validateInput(
        yarnData.gaugeColumnFrom,
        YarnParamName.GAUGE_COLUMN_FROM
    ) else false
    val gaugeColumnToIsErrorFlg = if (yarnData.gaugeColumnTo != null) !validateInput(
        yarnData.gaugeColumnTo,
        YarnParamName.GAUGE_COLUMN_TO
    ) else false
    val gaugeRowFromIsErrorFlg = if (yarnData.gaugeRowFrom != null) !validateInput(
        yarnData.gaugeRowFrom,
        YarnParamName.GAUGE_ROW_FROM
    ) else false
    val gaugeRowToIsErrorFlg = if (yarnData.gaugeRowTo != null) !validateInput(
        yarnData.gaugeRowTo,
        YarnParamName.GAUGE_ROW_TO
    ) else false
    val gaugeStitchIsErrorFlg =
        !validateInput(yarnData.gaugeStitch, YarnParamName.GAUGE_STITCH)
    val needleSizeFromIsErrorFlg = if (yarnData.needleSizeFrom != null) !validateInput(
        yarnData.needleSizeFrom,
        YarnParamName.NEEDLE_SIZE_FROM
    ) else false
    val needleSizeToIsErrorFlg = if (yarnData.needleSizeTo != null) !validateInput(
        yarnData.needleSizeTo,
        YarnParamName.NEEDLE_SIZE_TO
    ) else false
    val crochetNeedleSizeFromIsErrorFlg =
        if (yarnData.crochetNeedleSizeFrom != null) !validateInput(
            yarnData.crochetNeedleSizeFrom,
            YarnParamName.CROCHET_NEEDLE_SIZE_FROM
        ) else false
    val crochetNeedleSizeToIsErrorFlg =
        if (yarnData.crochetNeedleSizeTo != null) !validateInput(
            yarnData.crochetNeedleSizeTo,
            YarnParamName.CROCHET_NEEDLE_SIZE_TO
        ) else false
    val yarnDescriptionIsErrorFlg =
        !validateInput(yarnData.yarnDescription, YarnParamName.YARN_DESCRIPTION)

    validateInput(
        !yarnMakerNameIsErrorFlg
                && !yarnNameIsErrorFlg
                && !havingNumberIsErrorFlg
                && !qualityIsErrorFlg
                && !weightIsErrorFlg
                && !lengthIsErrorFlg
                && !colorNumberIsErrorFlg
                && !rotNumberIsErrorFlg
                && !gaugeColumnFromIsErrorFlg
                && !gaugeColumnToIsErrorFlg
                && !gaugeRowFromIsErrorFlg
                && !gaugeRowToIsErrorFlg
                && !gaugeStitchIsErrorFlg
                && !needleSizeFromIsErrorFlg
                && !needleSizeToIsErrorFlg
                && !crochetNeedleSizeFromIsErrorFlg
                && !crochetNeedleSizeToIsErrorFlg
                && !yarnDescriptionIsErrorFlg
    )

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
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.YARN_MAKER_NAME
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.yarnMakerName,
                onValueChange = { updateYarnEditData(it, YarnParamName.YARN_MAKER_NAME) },
                isError = yarnMakerNameIsErrorFlg,
                supportingText = { Text(text = if (yarnMakerNameIsErrorFlg) "エラーがあります" else "" + "(${yarnData.yarnMakerName.length}/${YarnParamName.YARN_MAKER_NAME.maxLength})") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )

            TextField(
                label = { Text("名前", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.YARN_NAME
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.yarnName,
                onValueChange = { updateYarnEditData(it, YarnParamName.YARN_NAME) },
                isError = yarnNameIsErrorFlg,
                supportingText = { Text(text = (if (yarnNameIsErrorFlg) "エラー" else "") + "(${yarnData.yarnName.length}/${YarnParamName.YARN_NAME.maxLength})") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            // Image Picker設定
            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                if (it != null) {
                    updateYarnEditData(it.toString(),YarnParamName.IMAGE_URL)
//                    Log.d("PhotoPicker", "Selected URI: $it")
                } else {
//                    Log.d("PhotoPicker", "No media selected")
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 2.dp, bottom = 10.dp)
                    .height(140.dp)
                    .width(140.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
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
                            alpha = 0.7F,
                            modifier = Modifier.fillMaxSize()
                        )

                    else -> AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(yarnData.imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alpha = 0.7F,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                IconButton(onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Icon(painterResource(R.drawable.outline_image_24), contentDescription = null, modifier = Modifier.size(50.dp))
                }
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
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            0,
                            YarnParamName.HAVING_NUMBER
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = DecimalFormat("#.#").format(yarnData.havingNumber),
                onValueChange = { updateYarnEditData(it, YarnParamName.HAVING_NUMBER) },
                isError = havingNumberIsErrorFlg,
                supportingText = { Text(text = if (havingNumberIsErrorFlg) "エラー" else "") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
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
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.QUALITY
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.quality,
                onValueChange = { updateYarnEditData(it, YarnParamName.QUALITY) },
                isError = qualityIsErrorFlg,
                supportingText = { Text(text = (if (qualityIsErrorFlg) "エラー" else "") + "(${yarnData.quality.length}/${YarnParamName.QUALITY.maxLength})") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            TextField(
                label = { Text("重量(単位：g)", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.WEIGHT
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = if (yarnData.weight != null) DecimalFormat("#.#").format(yarnData.weight) else "",
                onValueChange = { updateYarnEditData(it, YarnParamName.WEIGHT) },
                isError = weightIsErrorFlg,
                supportingText = { Text(text = if (weightIsErrorFlg) "エラー" else "") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            TextField(
                label = { Text("糸長(単位：m)", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.LENGTH
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = if (yarnData.length != null) DecimalFormat("#.#").format(yarnData.length) else "",
                onValueChange = { updateYarnEditData(it, YarnParamName.LENGTH) },
                isError = lengthIsErrorFlg,
                supportingText = { Text(text = if (lengthIsErrorFlg) "エラー" else "") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "玉巻 or かせ：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 5.dp, start = 10.dp, bottom = 5.dp)
            )
            Row(
                Modifier
                    .selectableGroup()
                    .align(Alignment.Start)
            ) {
                YarnRoll.entries.forEach {
                    Row(
                        Modifier
//                            .fillMaxWidth()
//                            .height(56.dp)
                            .selectable(
                                selected = (it == yarnData.roll),
                                onClick = { updateYarnEditData(it, YarnParamName.ROLL) },
                                role = Role.RadioButton
                            )
                            .padding(top = 0.dp, start = 10.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (it == yarnData.roll),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = if (it.value == "") "未設定" else it.value,
                            style = MaterialTheme.typography.labelSmall,
//                            modifier = Modifier
//                                .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                        )
                    }
                }
            }
            Text(
                text = "糸の太さ：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 5.dp, start = 10.dp, bottom = 5.dp)
            )
            Text(
                text = if (yarnData.thickness.value == "") "未設定" else yarnData.thickness.value,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            )
            Slider(
                value = yarnData.thickness.rowValue.toFloat(),
                onValueChange = {
                    updateYarnEditData(
                        YarnThickness.entries.first { yarnThicknessItem -> Math.round(it) == yarnThicknessItem.rowValue },
                        YarnParamName.THICKNESS
                    )
                },
                steps = YarnThickness.entries.size - 2,
                valueRange = 0f..(YarnThickness.entries.size - 1).toFloat(),
                modifier = Modifier
                    .padding(top = 0.dp, start = 30.dp, end = 30.dp, bottom = 5.dp)
            )
            TextField(
                label = { Text("色番号", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.COLOR_NUMBER
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.colorNumber,
                onValueChange = { updateYarnEditData(it, YarnParamName.COLOR_NUMBER) },
                isError = colorNumberIsErrorFlg,
                supportingText = { Text(text = if (colorNumberIsErrorFlg) "エラー" else "") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            TextField(
                label = { Text("ロット番号", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.ROT_NUMBER
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.rotNumber,
                onValueChange = { updateYarnEditData(it, YarnParamName.ROT_NUMBER) },
                isError = rotNumberIsErrorFlg,
                supportingText = { Text(text = if (rotNumberIsErrorFlg) "エラー" else "") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.GAUGE_COLUMN_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.gaugeColumnFrom != null) DecimalFormat("#.#").format(
                        yarnData.gaugeColumnFrom
                    ) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_COLUMN_FROM) },
                    isError = gaugeColumnFromIsErrorFlg,
                    supportingText = { Text(text = if (gaugeColumnFromIsErrorFlg) "エラー" else "") },
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.GAUGE_COLUMN_TO
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.gaugeColumnTo != null) DecimalFormat("#.#").format(yarnData.gaugeColumnTo) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_COLUMN_TO) },
                    isError = gaugeColumnToIsErrorFlg,
                    supportingText = { Text(text = if (gaugeColumnToIsErrorFlg) "エラー" else "") },
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.GAUGE_ROW_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.gaugeRowFrom != null) DecimalFormat("#.#").format(yarnData.gaugeRowFrom) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_ROW_FROM) },
                    isError = gaugeRowFromIsErrorFlg,
                    supportingText = { Text(text = if (gaugeRowFromIsErrorFlg) "エラー" else "") },
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.GAUGE_ROW_TO
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.gaugeRowTo != null) DecimalFormat("#.#").format(yarnData.gaugeRowTo) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_ROW_TO) },
                    isError = gaugeRowToIsErrorFlg,
                    supportingText = { Text(text = if (gaugeRowToIsErrorFlg) "エラー" else "") },
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
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.GAUGE_STITCH
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.gaugeStitch,
                onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_STITCH) },
                isError = gaugeStitchIsErrorFlg,
                supportingText = { Text(text = if (gaugeStitchIsErrorFlg) "エラー" else "") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.NEEDLE_SIZE_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.needleSizeFrom != null) DecimalFormat("#.#").format(
                        yarnData.needleSizeFrom
                    ) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.NEEDLE_SIZE_FROM) },
                    isError = needleSizeFromIsErrorFlg,
                    supportingText = { Text(text = if (needleSizeFromIsErrorFlg) "エラー" else "") },
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.NEEDLE_SIZE_TO
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.needleSizeTo != null) DecimalFormat("#.#").format(yarnData.needleSizeTo) else "",
                    onValueChange = { updateYarnEditData(it, YarnParamName.NEEDLE_SIZE_TO) },
                    isError = needleSizeToIsErrorFlg,
                    supportingText = { Text(text = if (needleSizeToIsErrorFlg) "エラー" else "") },
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.CROCHET_NEEDLE_SIZE_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.crochetNeedleSizeFrom != null) DecimalFormat("#.#").format(
                        yarnData.crochetNeedleSizeFrom
                    ) else "",
                    onValueChange = {
                        updateYarnEditData(
                            it,
                            YarnParamName.CROCHET_NEEDLE_SIZE_FROM
                        )
                    },
                    isError = crochetNeedleSizeFromIsErrorFlg,
                    supportingText = { Text(text = if (crochetNeedleSizeFromIsErrorFlg) "エラー" else "") },
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
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.CROCHET_NEEDLE_SIZE_TO
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = if (yarnData.crochetNeedleSizeTo != null) DecimalFormat("#.#").format(
                        yarnData.crochetNeedleSizeTo
                    ) else "",
                    onValueChange = {
                        updateYarnEditData(
                            it,
                            YarnParamName.CROCHET_NEEDLE_SIZE_TO
                        )
                    },
                    isError = crochetNeedleSizeToIsErrorFlg,
                    supportingText = { Text(text = if (crochetNeedleSizeToIsErrorFlg) "エラー" else "") },
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
                label = { Text("JANコード", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                value = yarnData.janCode,
                onValueChange = { updateYarnEditData(it, YarnParamName.JAN_CODE) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                enabled = false,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
            TextField(
                label = { Text("メモ", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "",
                            YarnParamName.YARN_DESCRIPTION
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.yarnDescription,
                onValueChange = { updateYarnEditData(it, YarnParamName.YARN_DESCRIPTION) },
                isError = yarnDescriptionIsErrorFlg,
                supportingText = { Text(text = (if (yarnDescriptionIsErrorFlg) "エラー" else "") + "(${yarnData.yarnDescription.length}/${YarnParamName.YARN_DESCRIPTION.maxLength})") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                textStyle = MaterialTheme.typography.displayMedium,
                singleLine = false,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            )
        }
//        Spacer(modifier = Modifier.weight(1.0F))
    }
}

@Composable
fun YarnEditScreenBottom(
    yarnEditScreenUiState: YarnEditScreenUiState,
    nextButtonOnClick: (YarnDataForScreen) -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
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
                        yarnEditScreenUiState.yarnEditData
                    )
                )
            },
            enabled = yarnEditScreenUiState.validateInputFlag
        ) {
            Text(text = "Next", style = MaterialTheme.typography.labelSmall)
        }
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
            validateInput = { _ -> },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}

@Preview
@Composable
fun YarnEditScreenBottomPreview() {
    YarnShelfTheme {
        YarnEditScreenBottom(
            YarnEditScreenUiState(
                yarnEditData =
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
                validateInputFlag = true
            ),
            nextButtonOnClick = { _ -> },
            cancelButtonOnClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}