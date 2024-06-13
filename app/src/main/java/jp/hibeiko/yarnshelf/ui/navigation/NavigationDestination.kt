package jp.hibeiko.yarnshelf.ui.navigation

// 画面遷移のための画面情報定義（インターフェース）
interface NavigationDestination {
    // 画面パス
    val route: String
    // 画面名
    val title: String
}