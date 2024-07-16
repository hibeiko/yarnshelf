package jp.hibeiko.yarnshef.ui

import androidx.lifecycle.SavedStateHandle
import jp.hibeiko.yarnshef.dummy.YarnDataDummyRepository
import jp.hibeiko.yarnshef.dummy.YarnDummyData
import jp.hibeiko.yarnshef.rules.TestDispatcherRule
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.YarnConfirmDestination
import jp.hibeiko.yarnshelf.ui.YarnConfirmScreenViewModel
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// 毛糸情報編集確認画面
class YarnConfirmScreenViewModelUnitTest {
    // 単体テストが Android UI スレッドに対応していないことです。単体テストは、Android デバイスやエミュレータではなく、ワークステーションで実行されます。ローカル単体テストのコードが Main ディスパッチャを参照する場合、単体テストの実行時に例外（上記の例など）がスローされます。
    // Main ディスパッチャは UI コンテキストでのみ使用できるため、ディスパッチャを単体テストに適したものに置き換える必要があります。Kotlin コルーチン ライブラリには、この目的のために TestDispatcher というコルーチン ディスパッチャが用意されています。
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var viewModel: YarnConfirmScreenViewModel
    private lateinit var repository: YarnDataDummyRepository

    @Before
    fun setUp() {
        repository = YarnDataDummyRepository()
        val savedStateHandle = SavedStateHandle(
            mapOf(
                YarnConfirmDestination.entryItemArg to YarnDataForScreen(
                    yarnId = 0,
                    janCode = "9999999999999",
                    yarnName = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    yarnMakerName = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    colorNumber = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    rotNumber = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    quality = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    weight = 10000.0,
                    roll = YarnRoll.BALL,
                    length = 10000.0,
                    gaugeColumnFrom = 100.0,
                    gaugeColumnTo = 100.0,
                    gaugeRowFrom = 100.0,
                    gaugeRowTo = 100.0,
                    gaugeStitch = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    needleSizeFrom = 30.0,
                    needleSizeTo = 30.0,
                    crochetNeedleSizeFrom = 30.0,
                    crochetNeedleSizeTo = 30.0,
                    thickness = YarnThickness.VERY_THICK,
                    havingNumber = 1000,
                    yarnDescription = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    imageUrl = "https://item-shopping.c.yimg.jp/i/g/ko-da_rambouillet-wc",
                    drawableResourceId = R.drawable.not_found,
                )
            )
        )
        viewModel = YarnConfirmScreenViewModel(
            savedStateHandle = savedStateHandle,
            yarnDataRepository = repository,
        )
    }

    // Destinationのテスト
    @Test
    fun yarnConfirmScreenViewModel_verifyDestination() {
        assertEquals(YarnConfirmDestination.title, "毛糸情報確認画面")
        assertEquals(YarnConfirmDestination.route, "YarnConfirmEdit")
    }

    // 初期表示状態のテスト
    @Test
    fun yarnConfirmScreenViewModel_InitialState() {
        // 初期設定した最大値が設定されること
        val yarnData: YarnData = viewModel.yarnConfirmScreenUiState.yarnConfirmData
        assertEquals(yarnData.yarnId, 0)
        assertEquals(yarnData.janCode, "9999999999999")
        assertEquals(
            yarnData.yarnName,
            "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０"
        )
        assertEquals(yarnData.yarnMakerName, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.colorNumber, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.rotNumber, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.quality, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.weight, 10000.0)
        assertEquals(yarnData.roll, YarnRoll.BALL)
        assertEquals(yarnData.length, 10000.0)
        assertEquals(yarnData.gaugeColumnFrom, 100.0)
        assertEquals(yarnData.gaugeColumnTo, 100.0)
        assertEquals(yarnData.gaugeRowFrom, 100.0)
        assertEquals(yarnData.gaugeRowTo, 100.0)
        assertEquals(yarnData.gaugeStitch, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.needleSizeFrom, 30.0)
        assertEquals(yarnData.needleSizeTo, 30.0)
        assertEquals(yarnData.crochetNeedleSizeFrom, 30.0)
        assertEquals(yarnData.crochetNeedleSizeTo, 30.0)
        assertEquals(yarnData.thickness, YarnThickness.VERY_THICK)
        assertEquals(yarnData.havingNumber, 1000)
        assertEquals(
            yarnData.yarnDescription,
            "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０"
        )
        assertEquals(yarnData.imageUrl, "https://item-shopping.c.yimg.jp/i/g/ko-da_rambouillet-wc")
        assertEquals(yarnData.drawableResourceId, R.drawable.not_found)

    }

    // 毛糸情報が更新されること
    @Test
    fun yarnConfirmScreenViewModel_UpdateYarnData_verifySuccess() {
        viewModel.updateYarnData()
        assertEquals(viewModel.yarnConfirmScreenUiState.yarnConfirmData,
            YarnDummyData.dummyDataList.first { it.yarnId == viewModel.yarnConfirmScreenUiState.yarnConfirmData.yarnId })
    }
}