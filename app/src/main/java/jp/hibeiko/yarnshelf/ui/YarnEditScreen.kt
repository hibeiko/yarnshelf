package jp.hibeiko.yarnshelf.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme

object YarnEditDestination : NavigationDestination {
    override val route = "YarnInfoEdit"
    override val title = "けいとを編集"
    const val yarnIdArg = "yarnId"
    val routeWithArgs = "${route}/{$yarnIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnEditScreen(
    modifier: Modifier = Modifier,
    // ViewModel(UiStateを使うため)
    yarnEditScreenViewModel: YarnEditScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: () -> Unit,
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
                                contentDescription = stringResource(R.string.back),
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
                    yarnEditScreenViewModel.yarnEditScreenUiState.isErrorMap,
                    modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1.0f, false)
                )
                YarnEditScreenBottom(
                    yarnEditScreenViewModel.yarnEditScreenUiState.isErrorMap,
                    yarnEditScreenViewModel::updateDialogViewFlag,
                    cancelButtonOnClick,
                    modifier
                )
            }
            if (yarnEditScreenViewModel.yarnEditScreenUiState.confirmDialogViewFlag) {
                ConfirmationDialog(
                    onDismissRequest = yarnEditScreenViewModel::updateDialogViewFlag,
                    onConfirmation = {
                        yarnEditScreenViewModel.updateYarnData()
                        nextButtonOnClick()
                    },
                    titleText = "確認",
                    dialogText = "この内容で更新します。よろしいですか？",
                    confirmButtonText = stringResource(R.string.ok)
                )

            }
        }
    }
}

@Composable
fun YarnEditScreenBody(
    yarnData: YarnDataForScreen,
    updateYarnEditData: (Any, YarnParamName) -> Unit,
    isErrorMap: Map<YarnParamName, String>,
    modifier: Modifier = Modifier
) {
//    Log.d("YarnEditScreen", "$yarnData")

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
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
                isError = isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.YARN_MAKER_NAME,
                            ""
                        ) + "(${yarnData.yarnMakerName.length}/${YarnParamName.YARN_MAKER_NAME.maxLength})"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )

            OutlinedTextField(
                label = {
                    Text(
                        stringResource(R.string.yarneditscreen_input_name_field_name),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
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
                isError = isErrorMap.containsKey(YarnParamName.YARN_NAME),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.YARN_NAME,
                            ""
                        ) + "(${yarnData.yarnName.length}/${YarnParamName.YARN_NAME.maxLength})"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            // Image Picker設定
            val context = LocalContext.current
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                    if (it != null) {
                        // 権限の永続化
                        context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION )
                        updateYarnEditData(it.toString(), YarnParamName.IMAGE_URL)
//                    Log.d("PhotoPicker", "Selected URI: $it")
                    } else {
//                    Log.d("PhotoPicker", "No media selected")
                    }
                }
            Box(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 12.dp)
                    .height(140.dp)
                    .width(140.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                when (yarnData.imageUrl) {
                    "" ->
                        Image(
                            painter = painterResource(R.drawable.not_found),
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
                        error = painterResource(R.drawable.not_found),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alpha = 0.7F,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                IconButton(
                    onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Icon(
                        painterResource(R.drawable.outline_image_24),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = { Text("残量(単位：玉)", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        updateYarnEditData(
                            "0",
                            YarnParamName.HAVING_NUMBER
                        )
                    }) { Icon(Icons.Default.Clear, contentDescription = null) }
                },
                value = yarnData.havingNumber,
                onValueChange = { updateYarnEditData(it, YarnParamName.HAVING_NUMBER) },
                isError = isErrorMap.containsKey(YarnParamName.HAVING_NUMBER),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.HAVING_NUMBER,
                            ""
                        )
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
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
            OutlinedTextField(
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
                isError = isErrorMap.containsKey(YarnParamName.QUALITY),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.QUALITY,
                            ""
                        ) + "(${yarnData.quality.length}/${YarnParamName.QUALITY.maxLength})"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
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
                value = yarnData.weight,
                onValueChange = { updateYarnEditData(it, YarnParamName.WEIGHT) },
                isError = isErrorMap.containsKey(YarnParamName.WEIGHT),
                supportingText = { Text(text = isErrorMap.getOrDefault(YarnParamName.WEIGHT, "")) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
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
                value = yarnData.length,
                onValueChange = { updateYarnEditData(it, YarnParamName.LENGTH) },
                isError = isErrorMap.containsKey(YarnParamName.LENGTH),
                supportingText = { Text(text = isErrorMap.getOrDefault(YarnParamName.LENGTH, "")) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "玉巻 or かせ：",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 6.dp, start = 12.dp, bottom = 6.dp)
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
                            .padding(top = 0.dp, start = 12.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (it == yarnData.roll),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = if (it.value == "") "未設定" else it.value,
                            style = MaterialTheme.typography.displayMedium,
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
                    .padding(top = 12.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = if (yarnData.thickness.value == "") "未設定" else yarnData.thickness.value,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 0.dp, start = 12.dp, bottom = 6.dp)
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
                    .padding(top = 0.dp, start = 32.dp, end = 32.dp, bottom = 6.dp)
            )
            OutlinedTextField(
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
                isError = isErrorMap.containsKey(YarnParamName.COLOR_NUMBER),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.COLOR_NUMBER,
                            ""
                        ) + "(${yarnData.colorNumber.length}/${YarnParamName.COLOR_NUMBER.maxLength})"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
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
                isError = isErrorMap.containsKey(YarnParamName.ROT_NUMBER),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.ROT_NUMBER,
                            ""
                        ) + "(${yarnData.rotNumber.length}/${YarnParamName.ROT_NUMBER.maxLength})"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "ゲージ",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 6.dp, start = 12.dp, bottom = 6.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
//                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
//                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.GAUGE_COLUMN_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = yarnData.gaugeColumnFrom,
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_COLUMN_FROM) },
                    isError = isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.GAUGE_COLUMN_FROM,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 12.dp, bottom = 6.dp)
                )
                OutlinedTextField(
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
                    value = yarnData.gaugeColumnTo,
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_COLUMN_TO) },
                    isError = isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.GAUGE_COLUMN_TO,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    enabled = yarnData.gaugeColumnFrom.isNotBlank(),
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
                )
                Text(
                    text = "目",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    //                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
//                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.GAUGE_ROW_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = yarnData.gaugeRowFrom,
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_ROW_FROM) },
                    isError = isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.GAUGE_ROW_FROM,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                )
                OutlinedTextField(
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
                    value = yarnData.gaugeRowTo,
                    onValueChange = { updateYarnEditData(it, YarnParamName.GAUGE_ROW_TO) },
                    isError = isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.GAUGE_ROW_TO,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    enabled = yarnData.gaugeRowFrom.isNotBlank(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
                )
                Text(
                    text = "段",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                )
            }
            OutlinedTextField(
                label = {
                    Text(
                        "ゲージの編み方(例：メリヤス編み)",
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
                isError = isErrorMap.containsKey(YarnParamName.GAUGE_STITCH),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.GAUGE_STITCH,
                            ""
                        ) + "(${yarnData.gaugeStitch.length}/${YarnParamName.GAUGE_STITCH.maxLength})"
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "参考使用針",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "棒針　",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                )
                OutlinedTextField(
//                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
//                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.NEEDLE_SIZE_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = yarnData.needleSizeFrom,
                    onValueChange = { updateYarnEditData(it, YarnParamName.NEEDLE_SIZE_FROM) },
                    isError = isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.NEEDLE_SIZE_FROM,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                )
                OutlinedTextField(
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
                    value = yarnData.needleSizeTo,
                    onValueChange = { updateYarnEditData(it, YarnParamName.NEEDLE_SIZE_TO) },
                    isError = isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.NEEDLE_SIZE_TO,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    enabled = yarnData.needleSizeFrom.isNotBlank(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
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
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                )
                OutlinedTextField(
                    //                label = { Text("ゲージ", style = MaterialTheme.typography.labelSmall) },
//                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            updateYarnEditData(
                                "",
                                YarnParamName.CROCHET_NEEDLE_SIZE_FROM
                            )
                        }) { Icon(Icons.Default.Clear, contentDescription = null) }
                    },
                    value = yarnData.crochetNeedleSizeFrom,
                    onValueChange = {
                        updateYarnEditData(
                            it,
                            YarnParamName.CROCHET_NEEDLE_SIZE_FROM
                        )
                    },
                    isError = isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.CROCHET_NEEDLE_SIZE_FROM,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
                )
                Text(
                    text = "～",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                )
                OutlinedTextField(
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
                    value = yarnData.crochetNeedleSizeTo,
                    onValueChange = {
                        updateYarnEditData(
                            it,
                            YarnParamName.CROCHET_NEEDLE_SIZE_TO
                        )
                    },
                    isError = isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO),
                    supportingText = {
                        Text(
                            text = isErrorMap.getOrDefault(
                                YarnParamName.CROCHET_NEEDLE_SIZE_TO,
                                ""
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    enabled = yarnData.crochetNeedleSizeFrom.isNotBlank(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(top = 0.dp, bottom = 6.dp, start = 12.dp)
                        .width(150.dp)
                )
            }
            OutlinedTextField(
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
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
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
                isError = isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION),
                supportingText = {
                    Text(
                        text = isErrorMap.getOrDefault(
                            YarnParamName.YARN_DESCRIPTION,
                            ""
                        )
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                textStyle = MaterialTheme.typography.displayMedium,
                singleLine = false,
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
            )
        }
//        Spacer(modifier = Modifier.weight(1.0F))
    }
}

@Composable
fun YarnEditScreenBottom(
    isErrorMap: Map<YarnParamName, String>,
    updateDialogViewFlag: (Boolean) -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier
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
            enabled = isErrorMap.isEmpty()
        ) {
            Text(text = stringResource(R.string.Submit), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview
@Composable
fun YarnEditScreenPreview() {
    YarnShelfTheme {
        YarnEditScreenBody(
            YarnDataForScreen(
                0,
                "10001",
                "1010 Seabright",
                "Jamieson's",
                "1010 Seabright",
                "1548",
                "シェットランドウール１００％",
                "25.01",
                YarnRoll.BALL,
                "105.0",
                "20.0",
                "21.0",
                "27.0",
                "28.0",
                "メリヤス編み",
                "3.0",
                "5.0",
                "0.0",
                "0.0",
                YarnThickness.THICK,
                "10",
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                "https://image.raku-uru.jp/01/19110/456/Spin+1010+Crpd_1625196651766.JPG",
                R.drawable.not_found
            ),
            updateYarnEditData = { _, _ -> },
            isErrorMap = mapOf(YarnParamName.YARN_MAKER_NAME to "エラーメッセージ"),
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
                isErrorMap = mutableMapOf(),
            updateDialogViewFlag = {_ ->},
            cancelButtonOnClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}