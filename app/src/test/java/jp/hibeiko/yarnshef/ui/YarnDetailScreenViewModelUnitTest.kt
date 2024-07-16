package jp.hibeiko.yarnshef.ui

import androidx.lifecycle.SavedStateHandle
import jp.hibeiko.yarnshef.dummy.YarnDataDummyRepository
import jp.hibeiko.yarnshef.dummy.YarnDummyData
import jp.hibeiko.yarnshef.rules.TestDispatcherRule
import jp.hibeiko.yarnshelf.ui.YarnDetailDestination
import jp.hibeiko.yarnshelf.ui.YarnDetailScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// 毛糸情報参照画面
class YarnDetailScreenViewModelUnitTest {
    // 単体テストが Android UI スレッドに対応していないことです。単体テストは、Android デバイスやエミュレータではなく、ワークステーションで実行されます。ローカル単体テストのコードが Main ディスパッチャを参照する場合、単体テストの実行時に例外（上記の例など）がスローされます。
    // Main ディスパッチャは UI コンテキストでのみ使用できるため、ディスパッチャを単体テストに適したものに置き換える必要があります。Kotlin コルーチン ライブラリには、この目的のために TestDispatcher というコルーチン ディスパッチャが用意されています。
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var viewModel: YarnDetailScreenViewModel
    private lateinit var repository: YarnDataDummyRepository

    @Before
    fun setUp() {
        repository = YarnDataDummyRepository()
        val savedStateHandle = SavedStateHandle(mapOf(YarnDetailDestination.yarnIdArg to 1))
        viewModel = YarnDetailScreenViewModel(
            savedStateHandle = savedStateHandle,
            yarnDataRepository = repository,
        )
    }

    // Destinationのテスト
    @Test
    fun yarnConfirmScreenViewModel_verifyDestination() {
        assertEquals(YarnDetailDestination.title, "けいと情報")
        assertEquals(YarnDetailDestination.route, "YarnInfoDetail")
    }

    // 初期表示状態のテスト
    @Test
    fun yarnDetailScreenViewModel_InitialState() = runTest {
        backgroundScope.launch {
            viewModel.yarnDetailScreenUiState.collect()
        }
        // 毛糸情報が設定されること
        assertEquals(
            YarnDummyData.dummyDataList.first { it.yarnId == 1 },
            viewModel.yarnDetailScreenUiState.value.yarnDetailData
        )
    }

    // 毛糸情報が削除されること
    @Test
    fun yarnDetailScreenViewModel_DeleteYarnData_verifySuccess() {
        val beforeSize = YarnDummyData.dummyDataList.size
        viewModel.deleteYarnData()
        assertFalse(YarnDummyData.dummyDataList.contains(viewModel.yarnDetailScreenUiState.value.yarnDetailData))
        assertEquals(beforeSize, YarnDummyData.dummyDataList.size + 1)
    }
}