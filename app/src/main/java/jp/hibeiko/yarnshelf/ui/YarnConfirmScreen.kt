package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme

object YarnConfirmDestination : NavigationDestination {
    override val route = "YarnConfirmEdit"
    override val title = "毛糸情報確認画面"
    const val yarnIdArg = "yarnId"
    const val yarnNameArg = "yarnName"
    const val yarnDescriptionArg = "yarnDescription"
    const val janCodeArg = "janCode"
    const val imageUrlArg = "imageUrl"
    const val drawableResourceIdArg = "drawableResourceId"
    val routeWithArgs =
        "${route}/{$yarnIdArg}?{$yarnNameArg}?{$yarnDescriptionArg}?{$janCodeArg}?{$imageUrlArg}?{$drawableResourceIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnConfirmScreen(
    // ViewModel(UiStateを使うため)
    yarnConfirmScreenViewModel: YarnConfirmScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    nextButtonOnClick: () -> Unit,
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
            YarnConfirmScreenBody(
                yarnConfirmScreenViewModel.yarnConfirmScreenUiState,
                yarnConfirmScreenViewModel::updateYarnData,
                nextButtonOnClick,
                cancelButtonOnClick,
                modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun YarnConfirmScreenBody(
    yarnConfirmScreenUiState: YarnConfirmScreenUiState,
    updateYarnData: () -> Unit,
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
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
                text = yarnConfirmScreenUiState.yarnConfirmData.yarnName,
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
                modifier = Modifier.padding(10.dp)
            )
            Text(
                text = yarnConfirmScreenUiState.yarnConfirmData.yarnDescription,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(10.dp)
            )
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
            Button(onClick = {
                updateYarnData()
                nextButtonOnClick()
            }) {
                Text(text = "OK", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview
@Composable
fun YarnConfirmScreenPreview() {
    YarnShelfTheme {
        YarnConfirmScreenBody(
            YarnConfirmScreenUiState(YarnData()),
            nextButtonOnClick = {},
            cancelButtonOnClick = {},
            updateYarnData = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}
