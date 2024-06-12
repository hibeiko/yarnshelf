package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// 画面間移動のためのルート定義
enum class YarnScreenRoot(val title: String) {
    Home(title = "けいとのたな"),           // ホーム画面
    YarnInfoEdit(title = "毛糸情報編集画面"),   // 毛糸情報編集画面
    YarnInfoConfirm(title = "毛糸情報確認画面"),    // 毛糸情報確認画面
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnShelfApp(
    // 共通で使うViewModel(UiStateを使うため)、画面遷移定義の作成
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // 共通で使うUiStateの作成
    val homeScreenUiState by homeScreenViewModel.homeScreenUiState.collectAsState()

    // 現在の画面を取得するために利用。
    val backStackEntry by navController.currentBackStackEntryAsState()
    // 戻り先画面を取得。戻り先画面がない場合はホーム画面をデフオルト設定する。
    val currentScreen = YarnScreenRoot.valueOf(
        backStackEntry?.destination?.route ?: YarnScreenRoot.Home.name
    )


    // 画面トップ
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
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
                            text = currentScreen.title,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    },
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "戻る", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            // ボディ画面。画面遷移定義とデータの受け渡しを行う。
            // NavHost定義
            NavHost(
                // navController: NavHostController クラスのインスタンス。このオブジェクトを使用して画面間を移動できます（navigate() メソッドを呼び出して別のデスティネーションに移動するなど）。NavHostController は、コンポーズ可能な関数から rememberNavController() を呼び出すことで取得できます。
                navController = navController,
                // startDestination: アプリが最初に NavHost を表示したときにデフォルトで表示されるデスティネーションを定義する文字列ルート。
                startDestination = YarnScreenRoot.Home.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                // ホーム画面。初期画面。
                composable(
                    // route: ルートの名前に対応する文字列。一意の文字列を指定できます。CupcakeScreen 列挙型の定数の name プロパティを使用します。
                    route = YarnScreenRoot.Home.name
                ) {
                    // content: ここで、所定のルートに対して表示するコンポーザブルを呼び出すことができます。
                    HomeScreen(
                        homeScreenUiState,
                        cardOnClick = homeScreenViewModel::cardOnClick,
                        dialogOnClick = homeScreenViewModel::dialogOnClick,
                        onEditButtonClicked = {
//                            homeScreenViewModel.dialogOnClick()
                            navController.navigate(YarnScreenRoot.YarnInfoEdit.name)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(innerPadding)
                    )
                }
                // 毛糸情報編集画面
                // Nextボタン→確認画面へ
                // Cancelボタン→ホーム画面ダイアログ表示状態へ
                composable(route = YarnScreenRoot.YarnInfoEdit.name) {
                    // Context は Android システムによって実装が提供される抽象クラスです。アプリケーション固有のリソースとクラスだけでなく、アプリケーション レベルのオペレーション（例: アクティビティの起動）のアップコールへのアクセスを可能にします。
//                    val context = LocalContext.current
                    YarnEditScreen(
                        homeScreenUiState,
                        yarnNameOnValueChange = homeScreenViewModel::yarnNameUpdate,
                        yarnDescriptionOnValueChange = homeScreenViewModel::yarnDescriptionUpdate,
                        nextButtonOnClick = { navController.navigate(YarnScreenRoot.YarnInfoConfirm.name) },
                        cancelButtonOnClick = {
//                            homeScreenViewModel.dialogOnClick()
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(innerPadding)
                    )
                }
                // 毛糸情報確認画面
                // OKボタン→ホーム画面へ
                // Cancelボタン→ホーム画面初期状態へ
                composable(route = YarnScreenRoot.YarnInfoConfirm.name) {
                    YarnConfirmScreen(
                        homeScreenUiState,
                        // システムの「戻る」ボタンとは異なり、[Cancel] ボタンを押しても前の画面に戻りません。バックスタックからすべての画面をポップ（削除）して、開始画面に戻る必要があります。
                        // route: 戻るデスティネーションのルートを表す文字列。
                        // inclusive: ブール値。true の場合、指定したルートもポップ（削除）します。false の場合、popBackStack() は開始デスティネーションより上にあるデスティネーションをすべて削除し、ユーザーが目にする一番上の画面として開始デスティネーションを残します。
                        okButtonOnClick = {
                            homeScreenViewModel.dialogOnClick()
                            navController.popBackStack(YarnScreenRoot.Home.name, false)
                        },
                        cancelButtonOnClick = { navController.navigateUp() },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(innerPadding)
                    )
                }
            }

        }
    }
}
