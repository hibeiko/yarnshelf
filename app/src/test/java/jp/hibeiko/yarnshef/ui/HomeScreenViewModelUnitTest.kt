package jp.hibeiko.yarnshef.ui

import jp.hibeiko.yarnshef.dummy.YarnDataDummyRepository
import jp.hibeiko.yarnshef.dummy.YarnDummyData
import jp.hibeiko.yarnshef.rules.TestDispatcherRule
import jp.hibeiko.yarnshelf.common.SortKey
import jp.hibeiko.yarnshelf.common.SortOrder
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.HomeDestination
import jp.hibeiko.yarnshelf.ui.HomeScreenUiState
import jp.hibeiko.yarnshelf.ui.HomeScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// ホーム画面
class HomeScreenViewModelUnitTest {
    // 単体テストが Android UI スレッドに対応していないことです。単体テストは、Android デバイスやエミュレータではなく、ワークステーションで実行されます。ローカル単体テストのコードが Main ディスパッチャを参照する場合、単体テストの実行時に例外（上記の例など）がスローされます。
    // Main ディスパッチャは UI コンテキストでのみ使用できるため、ディスパッチャを単体テストに適したものに置き換える必要があります。Kotlin コルーチン ライブラリには、この目的のために TestDispatcher というコルーチン ディスパッチャが用意されています。
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var repository: YarnDataDummyRepository

    @Before
    fun setUp() {
        repository = YarnDataDummyRepository()
        viewModel = HomeScreenViewModel(
            yarnDataRepository = repository,
        )
    }

    // Destinationのテスト
    @Test
    fun homeScreenViewModel_verifyDestination() {
        assertEquals(HomeDestination.title, "けいとのたな")
        assertEquals(HomeDestination.route, "Home")
    }

    // 初期表示時に全量が表示されること
    @Test
    fun homeScreenViewModel_InitialState_verifyYarnDataList() = runTest {
        // テスト中に少なくとも 1 つのコレクタが存在する必要があります。そうしないと、stateIn 演算子は基盤となる Flow の収集を開始せず、StateFlow の値は更新されません。
        backgroundScope.launch {
            viewModel.homeScreenUiState.collect()
        }
        // 検索条件の初期値のテスト
        assertEquals(viewModel.homeScreenSearchConditionState.value.query, "")
        assertEquals(viewModel.homeScreenSearchConditionState.value.sortKey, SortKey.YARN_NAME)
        assertEquals(viewModel.homeScreenSearchConditionState.value.sortOrder, SortOrder.ASC)

        // HomeScreenUiStateの初期値のテスト
        assertEquals(viewModel.homeScreenUiState.first(), HomeScreenUiState())

        repository.emitSelectAll("DUMMY")

        // 検索条件設定なし
        assertEquals(
            viewModel.homeScreenUiState.first().yarnDataList,
            YarnDummyData.dummyDataList.sortedBy { it.yarnName }
        )

        // 検索条件を設定
        viewModel.queryUpdate("101")
        assertEquals(viewModel.homeScreenSearchConditionState.value.query, "101")

        repository.emitSelectWithQuery("101", "DUMMY")
        assertEquals(
            viewModel.homeScreenUiState.first().yarnDataList,
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }
                .sortedBy { it.yarnName })

    }
    // ダイアログ表示非表示切替フラグを変更できること
    @Test
    fun homeScreenViewModel_UpdateDialogViewFlag_updateFlag() {
        val beforeFlag = viewModel.dialogViewFlag.value
        viewModel.dialogOnClick()
        assertEquals(viewModel.dialogViewFlag.value,!beforeFlag)
    }
    // カードをクリックすると表示対象のIDを変更できること
    @Test
    fun homeScreenViewModel_ClickCard_updateFlag() {
        val beforeFlag = viewModel.dialogViewFlag.value
        viewModel.cardOnClick(1)
        assertEquals(viewModel.dialogViewFlag.value,!beforeFlag)
        assertEquals(viewModel.dialogViewYarnId.value,1)
    }
    // ボトムシート表示非表示切替フラグを変更できること
    @Test
    fun homeScreenViewModel_UpdateBottomSheetViewFlag_updateFlag() {
        viewModel.bottomSheetOnClick(true)
        assertEquals(viewModel.bottomSheetViewFlag.value,true)
    }
    // 検索条件を変更できること
    @Test
    fun homeScreenViewModel_InputSearchCondition_updateCondition(){
        viewModel.queryUpdate("101")
        viewModel.sortKeyUpdate(SortKey.YARN_CREATE_DATE)
        viewModel.sortOrderUpdate()
        assertEquals(viewModel.homeScreenSearchConditionState.value.query, "101")
        assertEquals(viewModel.homeScreenSearchConditionState.value.sortKey, SortKey.YARN_CREATE_DATE)
        assertEquals(viewModel.homeScreenSearchConditionState.value.sortOrder, SortOrder.DESC)

        viewModel.sortOrderUpdate()
        assertEquals(viewModel.homeScreenSearchConditionState.value.sortOrder, SortOrder.ASC)
    }
    // 検索条件に合致するデータが表示されること
    @Test
    fun homeScreenViewModel_InputSearchCondition_verifyYarnDataList() = runTest {
        // テスト中に少なくとも 1 つのコレクタが存在する必要があります。そうしないと、stateIn 演算子は基盤となる Flow の収集を開始せず、StateFlow の値は更新されません。
        backgroundScope.launch {
            viewModel.homeScreenUiState.collect()
        }

        // HomeScreenUiStateの初期値のテスト
        assertEquals(viewModel.homeScreenUiState.first(), HomeScreenUiState())

        repository.emitSelectWithQuery("101", "DUMMY")
        assertEquals(
            viewModel.homeScreenUiState.first().yarnDataList,
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }
                .sortedBy { it.yarnName })
    }
}