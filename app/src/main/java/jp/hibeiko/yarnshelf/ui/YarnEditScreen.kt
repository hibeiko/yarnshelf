package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme

object YarnEditDestination : NavigationDestination {
    override val route = "YarnInfoEdit"
    override val title = "毛糸情報編集画面"
    const val yarnIdArg = "yarnId"
    val routeWithArgs = "${route}/{$yarnIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnEditScreen(
    // ViewModel(UiStateを使うため)
    yarnEditScreenViewModel: YarnEditScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: (String) -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
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
        ) { innerPadding ->
            YarnEditScreenBody(
                yarnEditScreenViewModel.yarnEditScreenUiState,
                nextButtonOnClick,
                cancelButtonOnClick,
                yarnEditScreenViewModel::updateYarnName,
                yarnEditScreenViewModel::updateYarnDescription,
                yarnEditScreenViewModel::validateInput,
                modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun YarnEditScreenBody(
    yarnEditScreenUiState: YarnEditScreenUiState,
    nextButtonOnClick: (String) -> Unit,
    cancelButtonOnClick: () -> Unit,
    updateYarnName: (String) -> Unit,
    updateYarnDescription: (String) -> Unit,
    validateInput: () -> Boolean,
    modifier: Modifier = Modifier
) {
//    Log.d("YarnEditScreen","${yarnEditScreenUiState.yarnEditData}")


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TextField(
            label = { Text("名前", style = MaterialTheme.typography.labelSmall) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
            value = yarnEditScreenUiState.yarnEditData.yarnName,
            onValueChange = { updateYarnName(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium
        )
        TextField(
            label = { Text("メモ", style = MaterialTheme.typography.labelSmall) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
            value = yarnEditScreenUiState.yarnEditData.yarnDescription,
            onValueChange = { updateYarnDescription(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.displayMedium,
            singleLine = false,
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
                nextButtonOnClick(
                    "${yarnEditScreenUiState.yarnEditData.yarnId}?${yarnEditScreenUiState.yarnEditData.yarnName}?${yarnEditScreenUiState.yarnEditData.yarnDescription}?${yarnEditScreenUiState.yarnEditData.janCode}?${yarnEditScreenUiState.yarnEditData.imageUrl}?${yarnEditScreenUiState.yarnEditData.drawableResourceId}",
                )},
                enabled =validateInput()
            ) {
                Text(text = "Next", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview
@Composable
fun YarnEditScreenPreview() {
    YarnShelfTheme {
        YarnEditScreenBody(
            YarnEditScreenUiState(YarnData()),
            nextButtonOnClick = {},
            cancelButtonOnClick = {},
            updateYarnName = {},
            updateYarnDescription = {},
            validateInput = {true},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}