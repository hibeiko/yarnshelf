package jp.hibeiko.yarnshelf.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.Coil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat

object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val title = "けいとのたな"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // ViewModel(UiStateを使うため)
    homeScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    editButtonOnClicked: (Int) -> Unit,
    addButtonOnClicked: () -> Unit,
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
                            text = HomeDestination.title,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    },
//                    navigationIcon = {
//                        if (navController.previousBackStackEntry != null) {
//                            IconButton(onClick = { navController.navigateUp() }) {
//                                Icon(
//                                    Icons.Filled.ArrowBack,
//                                    contentDescription = "戻る",
//                                    tint = MaterialTheme.colorScheme.primary
//                                )
//                            }
//                        }
//                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = addButtonOnClicked,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(
                            end = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateEndPadding(LocalLayoutDirection.current)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
        ) { innerPadding ->
            HomeScreenBody(
                homeScreenViewModel,
                editButtonOnClicked,
                modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun HomeScreenBody(
    // ViewModel(UiStateを使うため)
    homeScreenViewModel: HomeScreenViewModel,
    editButtonOnClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // UiStateを取得
    val homeScreenUiState by homeScreenViewModel.homeScreenUiState.collectAsState()
    val dialogViewFlag by homeScreenViewModel.dialogViewFlag.collectAsState()
    val dialogViewYarnId by homeScreenViewModel.dialogViewYarnId.collectAsState()


    LazyColumn(modifier = modifier) {
        items(homeScreenUiState.yarnDataList) { it ->
            YarnCard(
                it,
                cardOnClick = homeScreenViewModel::cardOnClick,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
    if (dialogViewFlag) {
        YarnDialog(
            homeScreenUiState.yarnDataList.first { it.yarnId == dialogViewYarnId },
            homeScreenViewModel::dialogOnClick,
            editButtonOnClicked,
        )
    }
}

@Composable
fun YarnCard(
    yarnData: YarnData,
    cardOnClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = { cardOnClick(yarnData.yarnId) },
    ) {
        val formatter = SimpleDateFormat("yyyy/MM/dd")

//        Log.d("HomeScreen","${yarnData}")

        Row {
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
                            .fillMaxHeight()
                            .width(140.dp),
                    )

                else -> AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(yarnData.imageUrl)
                        .crossfade(true)
                        .build(),
//    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(140.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(
                    text = yarnData.yarnName,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = yarnData.yarnDescription,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = "最終更新日：${formatter.format(yarnData.lastUpdateDate)}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End),
//                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun YarnDialog(
    yarnData: YarnData,
    dialogOnClick: () -> Unit,
    editButtonOnClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {
            dialogOnClick()
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        title = {
            Text(
                text = yarnData.yarnName,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                            modifier = Modifier
                                .height(140.dp)
                                .width(140.dp),
                        )

                    else -> AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(yarnData.imageUrl)
                            .crossfade(true)
                            .build(),
//    placeholder = painterResource(R.drawable.placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .width(140.dp),
                    )
                }
                Text(
                    text = yarnData.yarnDescription,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(2.dp)
                )
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    dialogOnClick()
                    editButtonOnClicked(yarnData.yarnId)
                }
            ) {
                Text(text = "詳細")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogOnClick()
                }
            ) {
                Text(text = "OK")
            }
        }
    )

}

@Preview
@Composable
fun HomeScreenPreview() {
    YarnShelfTheme {
        HomeScreen(
            editButtonOnClicked = {},
            addButtonOnClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}