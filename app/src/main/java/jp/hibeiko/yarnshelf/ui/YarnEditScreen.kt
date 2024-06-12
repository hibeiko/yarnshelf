package jp.hibeiko.yarnshelf.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.data.YarnData
import java.text.SimpleDateFormat
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnEditScreen(
    homeScreenUiState: HomeScreenUiState,
    yarnNameOnValueChange: (String) -> Unit,
    yarnDescriptionOnValueChange: (String) -> Unit,
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    ){
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .statusBarsPadding()
//        ) {
////        Text("This is Edit Screen")
//        Scaffold(
//            topBar = {
//                CenterAlignedTopAppBar(
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.surface,
//                        titleContentColor = MaterialTheme.colorScheme.primary,
//                    ),
//                    title = {
//                        Text(
//                            "毛糸情報編集画面",
//                            style = MaterialTheme.typography.headlineLarge,
//                        )
//                    }
//                )
//            }
//        ) { innerPadding ->
                    YarnEditScreenBody(
                        homeScreenUiState.yarnEditData,
                        yarnNameOnValueChange = yarnNameOnValueChange,
                        yarnDescriptionOnValueChange = yarnDescriptionOnValueChange,
                        nextButtonOnClick = nextButtonOnClick,
                        cancelButtonOnClick = cancelButtonOnClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
//                            .padding(innerPadding)
                    )
                }
//            }
//    }
@Composable
fun YarnEditScreenBody(
    yarnData: YarnData,
    yarnNameOnValueChange: (String) -> Unit,
    yarnDescriptionOnValueChange: (String) -> Unit,
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Column {
        Row {
            Log.d("YarnEditScreenBody","yarnData = ${yarnData.toString()}")
            Text(text = "名前")
            TextField(value = yarnData.yarnName, onValueChange = {yarnNameOnValueChange(it)})
        }
        Row {
            Text(text = "メモ")
            TextField(value = yarnData.yarnDescription, onValueChange = {yarnDescriptionOnValueChange((it))})
        }
        Row {
            Button(onClick = nextButtonOnClick) {
                Text(text = "Next")
            }
            Button(onClick = cancelButtonOnClick) {
                Text(text = "Cancel")
            }
        }
    }
}

