package jp.hibeiko.yarnshelf.ui

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme


object YarnEntryDestination : NavigationDestination {
    override val route = "YarnInfoEntry"
    override val title = "毛糸情報入力画面"
    const val searchItemArg = "searchItem"
    val routeWithArgs = "${route}/{$searchItemArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnEntryScreen(
    // ViewModel(UiStateを使うため)
    yarnEntryScreenViewModel: YarnEntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
//    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // UiStateを取得
    val yarnEntryScreenUiState by yarnEntryScreenViewModel.yarnEntryScreenUiState.collectAsState()

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
                            text = YarnEntryDestination.title,
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
            YarnEntryScreenBody(
                yarnEntryScreenUiState,
                yarnEntryScreenViewModel::yarnNameUpdate,
                yarnEntryScreenViewModel::yarnDescriptionUpdate,
                yarnEntryScreenViewModel::validateInput,
                yarnEntryScreenViewModel::saveYarnData,
                cancelButtonOnClick,
                modifier
                    .padding(innerPadding)
//                    .verticalScroll(rememberScrollState())
            )
        }
    }
}


@Composable
fun YarnEntryScreenBody(
    yarnEntryScreenUiState: YarnEntryScreenUiState,
    yarnNameUpdate: (String) -> Unit,
    yarnDescriptionUpdate: (String) -> Unit,
    validateInput: () -> Boolean,
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
    ) {
        TextField(
            label = { Text("名前 *", style = MaterialTheme.typography.labelSmall) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
            value = yarnEntryScreenUiState.yarnEntryData.yarnName,
            onValueChange = { yarnNameUpdate(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
        TextField(
            label = { Text("メモ *", style = MaterialTheme.typography.labelSmall) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
            value = yarnEntryScreenUiState.yarnEntryData.yarnDescription,
            onValueChange = { yarnDescriptionUpdate(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.displayMedium,
            maxLines = 5,
            modifier = Modifier.padding(top = 10.dp)
        )
        if (yarnEntryScreenUiState.yarnEntryData.imageUrl != "") {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(yarnEntryScreenUiState.yarnEntryData.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(140.dp)
                    .width(140.dp),
            )
        }
        Text(
            "* 必須項目",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 10.dp)
        )
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
                    nextButtonOnClick()
                    cancelButtonOnClick()
                },
                enabled = validateInput()
            ) {
                Text(text = "Next", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}


@Preview
@Composable
fun YarnEntryScreenPreview() {
    YarnShelfTheme {
        YarnEntryScreenBody(
            yarnEntryScreenUiState = YarnEntryScreenUiState(yarnEntryData = YarnData()),
            yarnNameUpdate = {},
            yarnDescriptionUpdate = {},
            validateInput = { true },
            nextButtonOnClick = {},
            cancelButtonOnClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}