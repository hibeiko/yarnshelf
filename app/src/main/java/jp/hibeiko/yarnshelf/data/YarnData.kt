package jp.hibeiko.yarnshelf.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import java.util.Date

@Entity
data class YarnData(
    @PrimaryKey(autoGenerate = true)
    // KEY * 必須項目
    val yarnId: Int = 0,
    // JANコード
    val janCode: String = "",
    // 名称 * 必須項目
    val yarnName: String = "",
    // メーカー
    val yarnMakerName: String = "",
    // 色番号
    val colorNumber: String = "",
    // ロット番号
    val rotNumber:String = "",
    // 品質
    val quality: String = "",
    // 重量
    val weight: Double? = null,
    // 巻き方
    val roll: YarnRoll = YarnRoll.NONE,
    // 長さ
    val length: Double? = null,
    // 標準ゲージ
    // 目 * Toに値を設定する場合はFromは必須
    val gaugeColumnFrom: Double? = null,
    val gaugeColumnTo: Double? = null,
    // 段 * Toに値を設定する場合はFromは必須
    val gaugeRowFrom: Double? = null,
    val gaugeRowTo: Double? = null,
    // 編み方
    val gaugeStitch: String = "",
    // 使用針
    // 棒針 * Toに値を設定する場合はFromは必須
    val needleSizeFrom: Double? = null,
    val needleSizeTo: Double? = null,
    // かぎ針 * Toに値を設定する場合はFromは必須
    val crochetNeedleSizeFrom: Double? = null,
    val crochetNeedleSizeTo: Double? = null,
    // 糸の太さ
    val thickness: YarnThickness = YarnThickness.NONE,
    // 個数 * 必須項目
    val havingNumber: Int = 0,
    // 備考
    val yarnDescription: String = "",
    // 最終更新日
    val lastUpdateDate: Date = Date(),
    // Yahooショッピングサイトの画像URL
    val imageUrl: String = "",
)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
//insert into YarnData values
//(0,'10001','1010 Seabright','Jamieson''s','1010 Seabright','1548','シェットランドウール１００％',25.01,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/456/Spin+1010+Crpd_1625196651766.JPG',2130968608),
//(1,'10002','Shaela','Jamieson''s','102 Shaela','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/35/Spin+102+Crpd_1625194839510.JPG',2130968602),
//(2,'10003','Night Hawk','Jamieson''s','1020 Night Hawk','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/454/Spin+1020+Crpd_1625196650659.JPG',2130968601),
//(3,'10004','Sholmit','Jamieson''s','103 Sholmit','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/31/Spin+103+Crpd_1625194837544.JPG',2130968603),
//(4,'10005','Natural White','Jamieson''s','104 Natural White','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/32/Spin+104+Crpd_1625194838195.JPG',2130968604),
//(5,'10006','Eesit','Jamieson''s','105 Eesit','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/30/Spin+105+Crpd_1625194836934.JPG',2130968605),
//(6,'10007','Mooskit','Jamieson''s','106 Mooskit','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/28/Spin+106+Crpd_1625194835979.JPG',2130968606),
//(7,'10008','Mogit','Jamieson''s','107 Mogit','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/26/Spin+107+Crpd_1625194834874.JPG',2130968607),
//(8,'10009','Natural Black','Jamieson''s','101 Natural Black(Shetland Black)','1548','シェットランドウール１００％',25.0,'BALL',105.0,20.0,21.0,27.0,28.0,'メリヤス編み',3.0,5.0,0.0,0.0,'THICK',10,'毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです',1718605302247,'https://image.raku-uru.jp/01/19110/38/Spin+101+Crpd_1625194841231.JPG',2130968600)
