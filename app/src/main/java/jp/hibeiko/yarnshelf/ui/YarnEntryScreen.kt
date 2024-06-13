package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme
import kotlinx.coroutines.launch
import java.util.Date

object YarnEntryDestination : NavigationDestination {
    override val route = "YarnInfoEntry"
    override val title = "あたらしい毛糸を登録"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnEntryScreen(
    // ViewModel(UiStateを使うため)
    yarnEntryScreenViewModel: YarnEntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier){
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
                        IconButton(onClick = cancelButtonOnClick ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "戻る",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    )
                },
            ) { innerPadding ->
                YarnEntryScreenBody(
                    yarnEntryScreenViewModel,
                    nextButtonOnClick,
                    cancelButtonOnClick,
                    yarnEntryScreenViewModel::validateInput,
                    modifier.padding(innerPadding)
                )
            }
        }
}

@Composable
fun YarnEntryScreenBody(
    // ViewModel(UiStateを使うため)
    yarnEntryScreenViewModel: YarnEntryScreenViewModel,
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    validateInput: () -> Boolean,
    modifier: Modifier = Modifier){
    // UiStateを取得
    val yarnEntryScreenUiState by yarnEntryScreenViewModel.yarnEntryScreenUiState.collectAsState()
    // DB処理実行のためのコルーチン
    // rememberCoroutineScope() は、呼び出されたコンポジションにバインドされた CoroutineScope を返すコンポーズ可能な関数です。コンポーザブルの外部でコルーチンを開始し、スコープがコンポジションから離れた後にコルーチンがキャンセルされるようにする場合、コンポーズ可能な関数 rememberCoroutineScope() を使用できます。この関数は、コルーチンのライフサイクルを手動で制御する必要がある場合（ユーザー イベントが発生するたびにアニメーションをキャンセルする場合など）に使用できます。
    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(start = 20.dp)
    ) {
        TextField(
            label = {Text("名前 *", style = MaterialTheme.typography.labelSmall)},
            leadingIcon = {Icon(Icons.Default.Edit, contentDescription = null)},
            value = yarnEntryScreenUiState.yarnEntryData.yarnName,
            onValueChange = {yarnEntryScreenViewModel.yarnNameUpdate(it)},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium
        )
        TextField(
            label = { Text("メモ *", style = MaterialTheme.typography.labelSmall)},
            leadingIcon = {Icon(Icons.Default.Edit, contentDescription = null)},
            value = yarnEntryScreenUiState.yarnEntryData.yarnDescription,
            onValueChange = {yarnEntryScreenViewModel.yarnDescriptionUpdate(it)},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.displayMedium,
            singleLine = false,
            modifier = Modifier.padding(top = 10.dp)
        )
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
//                onClick = nextButtonOnClick
                onClick = {
                    coroutineScope.launch {
                        yarnEntryScreenViewModel.saveYarnData()
                        cancelButtonOnClick()
                    }
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
        YarnEntryScreen(
            nextButtonOnClick = {},
            cancelButtonOnClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}