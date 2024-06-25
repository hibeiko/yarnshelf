package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme

object YarnConfirmDestination : NavigationDestination {
    override val route = "YarnConfirmEdit"
    override val title = "毛糸情報確認画面"
    const val entryItemArg = "entryItem"
    val routeWithArgs = "${route}/{$entryItemArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnConfirmScreen(
    modifier: Modifier = Modifier,
    // ViewModel(UiStateを使うため)
    yarnConfirmScreenViewModel: YarnConfirmScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
                            text = YarnConfirmDestination.title,
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
                        .weight(1.0f, false),
                    yarnConfirmScreenViewModel.yarnConfirmScreenUiState.yarnConfirmData,
                )
                YarnConfirmScreenBottom(
                    modifier,
                    yarnConfirmScreenViewModel::updateYarnData,
                    nextButtonOnClick,
                    cancelButtonOnClick,
                )
            }
        }
    }
}

@Composable
fun YarnConfirmScreenBottom(
    modifier: Modifier,
    updateYarnData: () -> Unit,
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
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
        Button(onClick = {
            updateYarnData()
            nextButtonOnClick()
        }) {
            Text(text = "OK", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview
@Composable
fun YarnConfirmBottomPreview() {
    YarnShelfTheme {
        YarnConfirmScreenBottom(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp),
            updateYarnData = {},
            nextButtonOnClick = {},
            cancelButtonOnClick = {}
        )
    }
}