package jp.naorin.yarnshelf.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jp.naorin.yarnshelf.ui.HomeDestination
import jp.naorin.yarnshelf.ui.HomeScreen
import jp.naorin.yarnshelf.ui.ItemSearchDestination
import jp.naorin.yarnshelf.ui.ItemSearchScreen
import jp.naorin.yarnshelf.ui.YarnDetailDestination
import jp.naorin.yarnshelf.ui.YarnDetailScreen
import jp.naorin.yarnshelf.ui.YarnEditDestination
import jp.naorin.yarnshelf.ui.YarnEditScreen

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
                    navController.navigate("${YarnDetailDestination.route}/${it}")
                },
                addButtonOnClicked = { navController.navigate(ItemSearchDestination.route) },
                modifier = modifier
            )
        }
        // 商品検索画面。
        composable(
            // route: ルートの名前に対応する文字列。一意の文字列を指定できます。CupcakeScreen 列挙型の定数の name プロパティを使用します。
            route = ItemSearchDestination.route
        ) {
            // content: ここで、所定のルートに対して表示するコンポーザブルを呼び出すことができます。
            ItemSearchScreen(
                // システムの「戻る」ボタンとは異なり、[Cancel] ボタンを押しても前の画面に戻りません。バックスタックからすべての画面をポップ（削除）して、開始画面に戻る必要があります。
                // route: 戻るデスティネーションのルートを表す文字列。
                // inclusive: ブール値。true の場合、指定したルートもポップ（削除）します。false の場合、popBackStack() は開始デスティネーションより上にあるデスティネーションをすべて削除し、ユーザーが目にする一番上の画面として開始デスティネーションを残します。
                nextButtonOnClick = { navController.popBackStack(HomeDestination.route, false) },
                cancelButtonOnClick = { navController.navigateUp() },
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
                nextButtonOnClick = { navController.popBackStack(HomeDestination.route, false) },
                cancelButtonOnClick = { navController.navigateUp() },
                modifier = modifier
            )
        }
    }
}