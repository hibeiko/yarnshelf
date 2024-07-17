package jp.hibeiko.yarnshef.common

import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.formatGaugeStringForScreen
import jp.hibeiko.yarnshelf.common.formatNeedleSizeStringForScreen
import jp.hibeiko.yarnshelf.common.formatWeightStringForScreen
import org.junit.Assert.assertEquals
import org.junit.Test


class YarnShelfAppCommonFunctionTest{
    // 標準状態重量（画面表示用）文字列
    @Test
    fun formatWeightStringForScreen_verifyDoubleFormat(){
        assertEquals(
            "10.4g玉巻(糸長約105.5m)",
            formatWeightStringForScreen(10.4,105.5,YarnRoll.BALL))
    }
    @Test
    fun formatWeightStringForScreen_verifyDecimalFormat(){
        assertEquals(
            "10gかせ(糸長約105m)",
            formatWeightStringForScreen(10.0,105.0,YarnRoll.SKEIN))
    }
    @Test
    fun formatWeightStringForScreen_verifyNull(){
        assertEquals(
            "-",
            formatWeightStringForScreen(null,null,YarnRoll.NONE))
    }
    // 標準ゲージ（画面表示用）文字列
    @Test
    fun formatGaugeStringForScreen_verifyDoubleFormat(){
        assertEquals(
            "20.5-21.5目27.5-28.5段(模様編み)",
            formatGaugeStringForScreen(20.5,21.5,27.5,28.5,"模様編み")
        )
    }
    @Test
    fun formatGaugeStringForScreen_verifyDecimalFormatAndPlainStitch(){
        assertEquals(
            "20-21目27-28段",
            formatGaugeStringForScreen(20.0,21.0,27.0,28.0,"メリヤス編み")
        )
    }
    @Test
    fun formatGaugeStringForScreen_verifyNoRange(){
        assertEquals(
            "20.5目27.5段(模様編み)",
            formatGaugeStringForScreen(20.5,null,27.5,null,"模様編み")
        )
    }
    @Test
    fun formatGaugeStringForScreen_verifyNull(){
        assertEquals(
            "-",
            formatGaugeStringForScreen(null,null,null,null,"")
        )
    }
    @Test
    fun formatNeedleSizeStringForScreen_verifyDoubleFormat(){
        assertEquals(
            "棒針8.5-10.5号\nかぎ針5.5-6.5号",
            formatNeedleSizeStringForScreen(8.5,10.5,5.5,6.5)
        )
    }
    @Test
    fun formatNeedleSizeStringForScreen_verifyDecimalFormatAndPlainStitch(){
        assertEquals(
            "棒針8-10号\nかぎ針5-6号",
            formatNeedleSizeStringForScreen(8.0,10.0,5.0,6.0)
        )
    }
    @Test
    fun formatNeedleSizeStringForScreen_verifyNoRange(){
        assertEquals(
            "棒針8.5号\nかぎ針5.5号",
            formatNeedleSizeStringForScreen(8.5,null,5.5,null)
        )
    }
    @Test
    fun formatNeedleSizeStringForScreen_verifyNull(){
        assertEquals(
            "-",
            formatNeedleSizeStringForScreen(null,null,null,null)
        )
    }
    @Test
    fun formatNeedleSizeStringForScreen_verifyCrochetNeedleSizeNull(){
        assertEquals(
            "棒針8.5号",
            formatNeedleSizeStringForScreen(8.5,null,null,null)
        )
    }
    @Test
    fun formatNeedleSizeStringForScreen_verifyNeedleSizeNull(){
        assertEquals(
            "かぎ針5.5号",
            formatNeedleSizeStringForScreen(null,null,5.5,null)
        )
    }
}
