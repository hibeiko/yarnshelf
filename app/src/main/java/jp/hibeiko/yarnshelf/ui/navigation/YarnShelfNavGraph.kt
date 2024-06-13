package jp.hibeiko.yarnshelf.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import jp.hibeiko.yarnshelf.ui.HomeDestination
import jp.hibeiko.yarnshelf.ui.HomeScreen
import jp.hibeiko.yarnshelf.ui.HomeScreenViewModel
import jp.hibeiko.yarnshelf.ui.YarnConfirmDestination
import jp.hibeiko.yarnshelf.ui.YarnConfirmScreen
import jp.hibeiko.yarnshelf.ui.YarnDetailDestination
import jp.hibeiko.yarnshelf.ui.YarnDetailScreen
import jp.hibeiko.yarnshelf.ui.YarnEditDestination
import jp.hibeiko.yarnshelf.ui.YarnEditScreen
import jp.hibeiko.yarnshelf.ui.YarnEntryDestination
import jp.hibeiko.yarnshelf.ui.YarnEntryScreen

@Composable
fun YarnShelfNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

//    // 現在の画面を取得するために利用。
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    // 戻り先画面を取得。戻り先画面がない場合はホーム画面をデフオルト設定する。
//    val currentScreen = backStackEntry?.destination?.route ?: HomeDestination.route
//    val currentScreen = YarnScreenRoot.valueOf(
//        backStackEntry?.destination?.route ?: YarnScreenRoot.Home.name
//    )


    // ボディ画面。画面遷移定義とデータの受け渡しを行う。
    // NavHost定義
    NavHost(
        // navController: NavHostController クラスのインスタンス。このオブジェクトを使用して画面間を移動できます（navigate() メソッドを呼び出して別のデスティネーションに移動するなど）。NavHostController は、コンポーズ可能な関数から rememberNavController() を呼び出すことで取得できます。
        navController = navController,
        // startDestination: アプリが最初に NavHost を表示したときにデフォルトで表示されるデスティネーションを定義する文字列ルート。
        startDestination = HomeDestination.route,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
    ) {
        // ホーム画面。初期画面。
        composable(
            // route: ルートの名前に対応する文字列。一意の文字列を指定できます。CupcakeScreen 列挙型の定数の name プロパティを使用します。
            route = HomeDestination.route
        ) {
            // content: ここで、所定のルートに対して表示するコンポーザブルを呼び出すことができます。
            HomeScreen(
                editButtonOnClicked = {
//                    navController.navigate("${YarnEditDestination.route}/${it}")
                    navController.navigate("${YarnDetailDestination.route}/${it}")
                },
                addButtonOnClicked = { navController.navigate(YarnEntryDestination.route) },
                modifier = modifier
            )
        }
        // 毛糸情報登録画面。
        composable(
            // route: ルートの名前に対応する文字列。一意の文字列を指定できます。CupcakeScreen 列挙型の定数の name プロパティを使用します。
            route = YarnEntryDestination.route
        ) {
            // content: ここで、所定のルートに対して表示するコンポーザブルを呼び出すことができます。
            YarnEntryScreen(
                nextButtonOnClick = { navController.navigate(YarnConfirmDestination.route) },
                cancelButtonOnClick = {
                    navController.navigateUp()
                },
                modifier = modifier
            )
        }
        // 毛糸情報詳細画面
        // Editボタン→編集画面へ
        // Cancelボタン→ホーム画面ダイアログ表示状態へ
        composable(
            route = YarnDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(YarnDetailDestination.yarnIdArg) {
                type = NavType.IntType
            })
        ) {
            YarnDetailScreen(
                nextButtonOnClick = { navController.navigate("${YarnEditDestination.route}/${it}") },
                cancelButtonOnClick = { navController.navigateUp() },
                modifier = modifier
            )
        }
        // 毛糸情報編集画面
        // Nextボタン→確認画面へ
        // Cancelボタン→ホーム画面ダイアログ表示状態へ
        composable(
            route = YarnEditDestination.routeWithArgs,
            arguments = listOf(navArgument(YarnEditDestination.yarnIdArg) {
                type = NavType.IntType
            })
        ) {
            // Context は Android システムによって実装が提供される抽象クラスです。アプリケーション固有のリソースとクラスだけでなく、アプリケーション レベルのオペレーション（例: アクティビティの起動）のアップコールへのアクセスを可能にします。
//                    val context = LocalContext.current
            YarnEditScreen(
//                        homeScreenUiState,
//                        yarnNameOnValueChange = homeScreenViewModel::yarnNameUpdate,
//                        yarnDescriptionOnValueChange = homeScreenViewModel::yarnDescriptionUpdate,
                nextButtonOnClick = { navController.navigate("${YarnConfirmDestination.route}/${it}") },
                cancelButtonOnClick = {
//                            homeScreenViewModel.dialogOnClick()
                    navController.navigateUp()
                },
                modifier = modifier
            )
        }
        // 毛糸情報確認画面
        // OKボタン→ホーム画面へ
        // Cancelボタン→ホーム画面初期状態へ
        composable(
            route = YarnConfirmDestination.routeWithArgs,
            arguments = listOf(
                navArgument(YarnConfirmDestination.yarnIdArg) {
                type = NavType.IntType
            },
                        navArgument(YarnConfirmDestination.yarnNameArg) {
                    type = NavType.StringType
                },
                navArgument(YarnConfirmDestination.yarnDescriptionArg) {
                    type = NavType.StringType
                }
            )
        ) {
            YarnConfirmScreen(
//                        homeScreenUiState,
                // システムの「戻る」ボタンとは異なり、[Cancel] ボタンを押しても前の画面に戻りません。バックスタックからすべての画面をポップ（削除）して、開始画面に戻る必要があります。
                // route: 戻るデスティネーションのルートを表す文字列。
                // inclusive: ブール値。true の場合、指定したルートもポップ（削除）します。false の場合、popBackStack() は開始デスティネーションより上にあるデスティネーションをすべて削除し、ユーザーが目にする一番上の画面として開始デスティネーションを残します。
                nextButtonOnClick = {
//                            homeScreenViewModel.dialogOnClick()
                    navController.popBackStack(HomeDestination.route, false)
                },
                cancelButtonOnClick = { navController.navigateUp() },
                modifier = modifier
            )
        }
    }
}