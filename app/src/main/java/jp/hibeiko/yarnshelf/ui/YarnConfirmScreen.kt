package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination

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
                        Button(onClick = {
                            yarnConfirmScreenViewModel.updateYarnData()
                            nextButtonOnClick()
                        }) {
                            Text(text = "OK", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                }

            }

        ) { innerPadding ->
            YarnDetailScreenBody(
                modifier.padding(innerPadding),
                yarnConfirmScreenViewModel.yarnConfirmScreenUiState.yarnConfirmData,
            )
        }
    }
}
