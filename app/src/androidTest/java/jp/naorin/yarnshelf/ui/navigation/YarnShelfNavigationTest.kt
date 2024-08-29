package jp.naorin.yarnshelf.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import jp.naorin.yarnshelf.R
import jp.naorin.yarnshelf.YarnShelfApp
import jp.naorin.yarnshelf.ui.HomeDestination
import jp.naorin.yarnshelf.ui.ItemSearchDestination
import jp.naorin.yarnshelf.ui.YarnDetailDestination
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class YarnShelfNavigationTest {
    // Compose Navigation をテストする場合、アプリコードと同じ NavHostController にはアクセスできません。ただし、TestNavHostController を使用し、このナビゲーション コントローラでテストルールを構成することはできます。
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // アプリを正しい場所に移動させるには、アプリが移動するためのアクションを行ったときに、TestNavHostController インスタンスを参照してナビゲーション ホストのナビゲーション ルートを確認する必要があります。
    // lateinit キーワードを使用して、オブジェクトの宣言後に初期化できるプロパティを宣言します。
    private lateinit var navController: TestNavHostController

    // テストはすべて、ナビゲーションの面をテストします。そのため、各テストは作成した TestNavHostController オブジェクトに依存します。Beforeアノテーションを使用してセットアップを自動的に行うことができます。
    @Before
    fun setupYarnShelfNavHost() {
        composeTestRule.setContent {
            // ナビゲーション ホストをセットアップします。
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            // その TestNavHostController を渡します。
            YarnShelfApp(navController = navController)
        }
    }

    // 毛糸登録画面で次へボタンを押下するとホーム画面に遷移すること
    @Test
    fun yarnShelfNavHost_yarnEntryScreen_clickNextButton_navigatesToHomeScreen() {
        addYarn()
        assertEquals(HomeDestination.route, navController.currentBackStackEntry?.destination?.route)
    }

    // 初期表示がホーム画面であること
    @Test
    fun yarnShelfNavHost_verifyStartDestination() {
        assertEquals(HomeDestination.route, navController.currentBackStackEntry?.destination?.route)
    }

    // ホーム画面で毛糸カードをクリックするとダイアログが表示されること
    @Test
    fun yarnShelfNavHost_homeScreen_clickCard_ViewDialog() {
        composeTestRule
            .onNodeWithText("テスト名前")
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.view_more))
            .isDisplayed()
    }
    // ホーム画面で毛糸カードをクリックして表示されるダイアログを閉じるとホーム画面に戻ること
    @Test
    fun yarnShelfNavHost_homeScreen_closeDialog_ViewHomeScreen() {
        composeTestRule
            .onNodeWithText("テスト名前")
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.close))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.view_more))
            .isNotDisplayed()
        assertEquals(HomeDestination.route, navController.currentBackStackEntry?.destination?.route)
    }

    // ホーム画面で毛糸カードをクリックして表示されるダイアログから毛糸情報詳細画面に遷移すること
    @Test
    fun yarnShelfNavHost_homeScreen_clickViewMore_navigatesToItemSearchScreen() {
        navigateToYarnDetailScreen()
        assertEquals(
            YarnDetailDestination.routeWithArgs,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    // ホーム画面で追加ボタンを押下すると毛糸検索画面に遷移すること
    @Test
    fun yarnShelfNavHost_homeScreen_clickAddIcon_navigatesToItemSearchScreen() {
        navigateToItemSearchScreen()
        assertEquals(
            ItemSearchDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    // 毛糸検索画面で戻るボタンを押下するとホーム画面に遷移すること
    @Test
    fun yarnShelfNavHost_itemSearchScreen_clickBackIcon_navigatesToHomeScreen() {
        navigateToItemSearchScreen()
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        assertEquals(HomeDestination.route, navController.currentBackStackEntry?.destination?.route)
    }

    // 毛糸検索画面で端末の戻るボタンを押下するとホーム画面に遷移すること
    @Test
    fun yarnShelfNavHost_itemSearchScreen_clickDeviceBackButton_navigatesToHomeScreen() {
        navigateToItemSearchScreen()
        assertEquals(
            HomeDestination.route,
            navController.previousBackStackEntry?.destination?.route
        )
    }

    // 毛糸検索画面で検索タブから商品を検索し、検索結果から一つをクリックすると、毛糸登録画面に遷移すること
//    @Test
//    fun yarnShelfNavHost_itemSearchScreen_clickSearchResultCard_navigatesToYarnEntryScreen(){
//        navigateToItemSearchScreen()
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.itemsearchscreen_itemsearch_tab_name))
//            .performClick()
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.itemsearchscreen_input_search_text))
//            .performTextInput("ユリカモヘヤ")
//        composeTestRule
//            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.itemsearchscreen_itemsearch_tab_name))
//            .performClick()
//        composeTestRule
//            .onNodeWithTag("ItemSearchScreen_SearchResultCard")
//            .performClick()
//        assertEquals(YarnEntryDestination.route, navController.currentBackStackEntry?.destination?.route)
//    }
//    // 毛糸検索画面で手入力タブを押下すると毛糸登録画面に遷移すること
//    @Test
//    fun yarnShelfNavHost_itemSearchScreen_clickManualInputTab_navigatesToYarnEntryScreen(){
//        navigateToYarnEntryScreen()
//        assertEquals(YarnEntryDestination.routeWithArgs, navController.currentBackStackEntry?.destination?.route)
//    }
    // 毛糸登録画面でトップバーの戻るボタンを押下すると毛糸検索画面に遷移すること
    @Test
    fun yarnShelfNavHost_yarnEntryScreen_clickBackIcon_navigatesToItemSearchScreen() {
        navigateToYarnEntryScreen()
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        assertEquals(
            ItemSearchDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    // 毛糸登録画面で端末の戻るボタンを押下するとホーム画面に遷移すること
    @Test
    fun yarnShelfNavHost_yarnEntryScreen_clickDeviceBackButton_navigatesToItemSearchScreen() {
        navigateToYarnEntryScreen()
        assertEquals(
            HomeDestination.route,
            navController.previousBackStackEntry?.destination?.route
        )
    }

    // 毛糸登録画面で画面下部の戻るボタンを押下するとホーム画面に遷移すること
    @Test
    fun yarnShelfNavHost_yarnEntryScreen_clickBackButton_navigatesToItemSearchScreen() {
        navigateToYarnEntryScreen()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.back))
            .performClick()
        assertEquals(
            ItemSearchDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    //    // 毛糸登録画面で次へボタンを押下すると毛糸登録確認画面に遷移すること
//    @Test
//    fun yarnShelfNavHost_yarnEntryScreen_clickNextButton_navigatesToItemSearchScreen(){
//        navigateToYarnConfirmScreen()
//        assertEquals(YarnConfirmDestination.routeWithArgs, navController.currentBackStackEntry?.destination?.route)
//    }
//    // 毛糸登録確認画面でトップバーの戻るボタンを押下すると毛糸登録画面に遷移すること
//    @Test
//    fun yarnShelfNavHost_yarnConfirmScreen_clickBackIcon_navigatesToYarnEntryScreen(){
//        navigateToYarnConfirmScreen()
//        composeTestRule
//            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
//            .performClick()
//        assertEquals(YarnEntryDestination.routeWithArgs, navController.currentBackStackEntry?.destination?.route)
//    }
//    // 毛糸登録確認画面で端末の戻るボタンを押下すると毛糸登録画面に遷移すること
//    @Test
//    fun yarnShelfNavHost_yarnConfirmScreen_clickDeviceBackButton_navigatesToYarnEntryScreen(){
//        navigateToYarnConfirmScreen()
//        assertEquals(YarnEntryDestination.routeWithArgs, navController.previousBackStackEntry?.destination?.route)
//    }
//    // 毛糸登録確認画面で画面下部の戻るボタンを押下すると毛糸登録画面に遷移すること
//    @Test
//    fun yarnShelfNavHost_yarnConfirmScreen_clickBackButton_navigatesToYarnEntryScreen(){
//        navigateToYarnConfirmScreen()
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.back))
//            .performClick()
//        assertEquals(YarnEntryDestination.routeWithArgs, navController.currentBackStackEntry?.destination?.route)
//    }


    // ヘルパー関数
    private fun navigateToYarnDetailScreen() {
        composeTestRule
            .onNodeWithText("テスト名前")
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.view_more))
            .performClick()
    }

    private fun navigateToItemSearchScreen() {
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.homescreen_add_button_name))
            .performClick()
    }

    private fun navigateToYarnEntryScreen() {
        navigateToItemSearchScreen()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.itemsearchscreen_manualinput_tab_name))
            .performClick()
    }

    private fun addYarn() {
        navigateToYarnEntryScreen()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.yarneditscreen_input_name_field_name))
            .performTextInput("テスト名前")
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.Submit))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.ok))
            .performClick()
    }

}
