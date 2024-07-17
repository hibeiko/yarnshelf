package jp.hibeiko.yarnshef.ui

import jp.hibeiko.yarnshef.data.YahooShoppingWebServiceItemDummyData
import jp.hibeiko.yarnshef.dummy.MLKitDummyErrorRepository
import jp.hibeiko.yarnshef.dummy.MLKitDummyRepository
import jp.hibeiko.yarnshef.dummy.YahooShoppingWebServiceItemSearchApiDummyRepository
import jp.hibeiko.yarnshef.rules.TestDispatcherRule
import jp.hibeiko.yarnshelf.ui.ItemSearchDestination
import jp.hibeiko.yarnshelf.ui.ItemSearchScreenViewModel
import jp.hibeiko.yarnshelf.ui.SearchItemUiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// 商品検索画面
class ItemSearchScreenViewModelUnitTest {
    // 単体テストが Android UI スレッドに対応していないことです。単体テストは、Android デバイスやエミュレータではなく、ワークステーションで実行されます。ローカル単体テストのコードが Main ディスパッチャを参照する場合、単体テストの実行時に例外（上記の例など）がスローされます。
    // Main ディスパッチャは UI コンテキストでのみ使用できるため、ディスパッチャを単体テストに適したものに置き換える必要があります。Kotlin コルーチン ライブラリには、この目的のために TestDispatcher というコルーチン ディスパッチャが用意されています。
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var viewModel: ItemSearchScreenViewModel

    @Before
    fun setUp() {
        viewModel = ItemSearchScreenViewModel(
            yahooShoppingWebServiceItemSearchApiRepository = YahooShoppingWebServiceItemSearchApiDummyRepository(),
            mlKitRepository = MLKitDummyRepository()
        )
    }

    // Destinationのテスト
    @Test
    fun itemSearchScreenViewModel_verifyDestination() {
        assertEquals(ItemSearchDestination.title, "あたらしい毛糸を登録")
        assertEquals(ItemSearchDestination.route, "ItemSearch")
    }

    // バーコードスキャンに成功すると検索結果が取得できること
    @Test
    fun itemSearchScreenViewModel_ScanBarcode_verifySuccess() {
        viewModel.readBarcode()
        assertEquals(
            viewModel.searchItemUiState,
            SearchItemUiState.Success(YahooShoppingWebServiceItemDummyData.dummyData)
        )
    }

    // 検索条件を設定できること
    @Test
    fun itemSearchScreenViewModel_InputSearchCondition_UpdateConditionAndValidateOK() {
        // 初期表示時は検索条件が空でValidationエラーとなる
        assertEquals(
            viewModel.itemSearchScreenUiState.value.yarnNameSearchInput,
            "")
        assertEquals(viewModel.validateYarnNameSearchInput(), false)

        viewModel.yarnNameSearchInputUpdate("ダルマ毛糸")
        assertEquals(
            viewModel.itemSearchScreenUiState.value.yarnNameSearchInput,
            "ダルマ毛糸")
        assertEquals(viewModel.validateYarnNameSearchInput(), true)

        // 検索実施
        viewModel.searchItem("ダルマ毛糸","")
        assertEquals(
            viewModel.searchItemUiState,
            SearchItemUiState.Success(YahooShoppingWebServiceItemDummyData.dummyData)
        )
    }

    // 検索条件を設定できること
    @Test
    fun itemSearchScreenViewModel_SearchErrorOccurred_UpdateUiState() {
        // 検索実施
        viewModel.yarnNameSearchInputUpdate("Error")
        viewModel.searchItem("Error","")
        assertEquals(
            viewModel.searchItemUiState,
            SearchItemUiState.Error
        )
    }

    // 検索タブを変更できること
    @Test
    fun itemSearchScreenViewModel_ChangeTabIndex_UpdateTabAndResetSearchCondition() {
        viewModel.yarnNameSearchInputUpdate("ダルマ毛糸")

        viewModel.selectedTabIndexUpdate(2)
        assertEquals(viewModel.itemSearchScreenUiState.value.selectedTabIndex,2)

        // 検索条件がリセットされる
        assertEquals(
            viewModel.itemSearchScreenUiState.value.yarnNameSearchInput,
            "")
        assertEquals(viewModel.validateYarnNameSearchInput(), false)
        assertEquals(viewModel.searchItemUiState, SearchItemUiState.Loading)
    }
}
