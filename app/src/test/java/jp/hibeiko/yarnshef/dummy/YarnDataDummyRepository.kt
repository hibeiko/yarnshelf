package jp.hibeiko.yarnshef.dummy

import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Date

class YarnDataDummyRepository : YarnDataRepository {
    private val flowList = MutableSharedFlow<List<YarnData>>()
    private val flow = MutableSharedFlow<YarnData>()

    private val yarnDummyData = YarnDummyData.dummyDataList

    override suspend fun insert(yarnData: YarnData) {
        yarnDummyData.removeIf { it.yarnId == yarnData.yarnId}
        yarnDummyData.add(yarnData)
    }

    override suspend fun update(yarnData: YarnData) {
        yarnDummyData.removeIf { it.yarnId == yarnData.yarnId}
        yarnDummyData.add(yarnData)
    }

    override suspend fun delete(yarnData: YarnData) {
        yarnDummyData.remove(yarnData)
    }

    override fun select(yarnId: Int): Flow<YarnData?> = flow
    suspend fun emitSelect(yarnId: Int) = flow.emit(
        yarnDummyData.first { it.yarnId == yarnId }
    )

    override fun selectAll(sortStr: String): Flow<List<YarnData>> = flowList
    suspend fun emitSelectAll(sortStr: String) = flowList.emit(
        // 名前の昇順
        yarnDummyData.sortedBy { it.yarnName }

    )

    override fun selectWithQuery(query: String, sortStr: String): Flow<List<YarnData>> = flowList
    suspend fun emitSelectWithQuery(query: String, sortStr: String) = flowList.emit(
        // 名前の昇順
        yarnDummyData.filter { it.yarnName.contains(query) }.sortedBy { it.yarnName }
    )
}

object YarnDummyData {
    val dummyDataList =
        mutableListOf(
            YarnData(
                1,
                "10001",
                "1010 Seabright",
                "Jamieson's",
                "1010 Seabright",
                "1548",
                "シェットランドウール１００％",
                25.01,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                10,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                2,
                "10002",
                "Shaela",
                "Jamieson's",
                "102 Shaela",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                17,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                3,
                "10003",
                "Night Hawk",
                "Jamieson's",
                "1020 Night Hawk",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                15,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                4,
                "10004",
                "Sholmit",
                "Jamieson's",
                "103 Sholmit",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                18,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                5,
                "10005",
                "Natural White",
                "Jamieson's",
                "104 Natural White",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                11,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                6,
                "10006",
                "Eesit",
                "Jamieson's",
                "105 Eesit",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                19,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                7,
                "10007",
                "Mooskit",
                "Jamieson's",
                "106 Mooskit",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                14,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                8,
                "10008",
                "Mogit",
                "Jamieson's",
                "107 Mogit",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                12,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
            YarnData(
                9,
                "10009",
                "Natural Black",
                "Jamieson's",
                "101 Natural Black(Shetland Black)",
                "1548",
                "シェットランドウール１００％",
                25.0,
                YarnRoll.BALL,
                105.0,
                20.0,
                21.0,
                27.0,
                28.0,
                "メリヤス編み",
                3.0,
                5.0,
                0.0,
                0.0,
                YarnThickness.THICK,
                13,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
            ),
        )
}