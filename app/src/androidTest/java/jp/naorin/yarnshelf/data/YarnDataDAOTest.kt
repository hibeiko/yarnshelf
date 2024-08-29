package jp.naorin.yarnshelf.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.naorin.yarnshelf.common.SortKey
import jp.naorin.yarnshelf.common.SortOrder
import jp.naorin.yarnshelf.common.YarnRoll
import jp.naorin.yarnshelf.common.YarnThickness
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date

@RunWith(AndroidJUnit4::class)
class YarnDataDAOTest {
    private lateinit var yarnDataDAO: YarnDataDAO
    private lateinit var yarnShelfDatabase: YarnShelfDatabase

    // 初期化処理：Database作成
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // この関数では、インメモリ データベースを使用し、ディスク上には保持しません。そのためには、inMemoryDatabaseBuilder() 関数を使用します。この処理を行うのは、情報を永続的に保持する必要はなく、プロセスが強制終了されたときに削除する必要があるためです。
        yarnShelfDatabase = Room.inMemoryDatabaseBuilder(context, YarnShelfDatabase::class.java)
            // テスト用に、.allowMainThreadQueries() を使用してメインスレッドで DAO クエリを実行しています。
            .allowMainThreadQueries()
            .build()
        yarnDataDAO = yarnShelfDatabase.yarnDataDAO()
    }

    // 終了処理：Databaseクローズ
    @After
    @Throws(IOException::class)
    fun closeDb() {
        yarnShelfDatabase.close()
    }

    // 1件登録できること
    // runBlocking{} を使用して新しいコルーチンでテストを実行します。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert1Item_insertsItemIntoDB() = runBlocking {
        yarnDataDAO.insert(YarnDummyData.dummyDataList[0])
        assertEquals(
            listOf(YarnDummyData.dummyDataList[0]), yarnDataDAO.selectAll("${SortKey.YARN_NAME.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 1件取得できること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_selectItemFromDBByYarnId() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList[4],
            yarnDataDAO.select(5 ).first(),
        )
    }
    // 更新できること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoUpdateItem_updateItemIntoDB() = runBlocking {
        yarnDataDAO.insert(YarnDummyData.dummyDataList[0])
        yarnDataDAO.update(YarnDummyData.dummyDataList[1].copy(yarnId = 1))
        assertEquals(
            listOf(YarnDummyData.dummyDataList[1].copy(yarnId = 1)),
            yarnDataDAO.selectAll("${SortKey.YARN_NAME.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 削除できること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoDeleteItem_deleteItemIntoDB() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)
        yarnDataDAO.delete(YarnDummyData.dummyDataList[4])
        assertEquals(
            YarnDummyData.dummyDataList.filter { it.yarnId != 5 }.sortedBy { it.yarnName },
            yarnDataDAO.selectAll("${SortKey.YARN_NAME.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 10件登録できること。名前の昇順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndOrderByNameAsc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.sortedBy { it.yarnName },
            yarnDataDAO.selectAll("${SortKey.YARN_NAME.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 10件登録できること。名前の降順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndOrderByNameDesc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.sortedByDescending { it.yarnName },
            yarnDataDAO.selectAll("${SortKey.YARN_NAME.rowValue} ${SortOrder.DESC.rowValue}").first(),
        )
    }
    // 10件登録できること。登録順の昇順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndOrderByCreateDateAsc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.sortedBy { it.yarnId },
            yarnDataDAO.selectAll("${SortKey.YARN_CREATE_DATE.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 10件登録できること。登録順の降順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndOrderByCreateDateDesc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.sortedByDescending { it.yarnId },
            yarnDataDAO.selectAll("${SortKey.YARN_CREATE_DATE.rowValue} ${SortOrder.DESC.rowValue}").first(),
        )
    }
    // 10件登録できること。所持数の昇順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndOrderByHavingNumberAsc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.sortedBy { it.havingNumber },
            yarnDataDAO.selectAll("${SortKey.YARN_HAVING_NUMBER.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 10件登録できること。所持数の降順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndOrderByHavingNumberDesc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.sortedByDescending { it.havingNumber },
            yarnDataDAO.selectAll("${SortKey.YARN_HAVING_NUMBER.rowValue} ${SortOrder.DESC.rowValue}").first(),
        )
    }
    // 10件登録できること。フィルター条件で抽出し名前の昇順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndSelectWithQueryOrderByNameAsc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }.sortedBy { it.yarnName },
            yarnDataDAO.selectWithQuery("101","${SortKey.YARN_NAME.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 10件登録できること。フィルター条件で抽出し名前の降順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndSelectWithQueryOrderByNameDesc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }.sortedByDescending { it.yarnName },
            yarnDataDAO.selectWithQuery("101","${SortKey.YARN_NAME.rowValue} ${SortOrder.DESC.rowValue}").first(),
        )
    }
    // 10件登録できること。フィルター条件で抽出し登録順の昇順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndSelectWithQueryOrderByCreateDateAsc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }.sortedBy { it.yarnId },
            yarnDataDAO.selectWithQuery("101","${SortKey.YARN_CREATE_DATE.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 10件登録できること。フィルター条件で抽出し登録順の降順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndSelectWithQueryOrderByCreateDateDesc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }.sortedByDescending { it.yarnId },
            yarnDataDAO.selectWithQuery("101","${SortKey.YARN_CREATE_DATE.rowValue} ${SortOrder.DESC.rowValue}").first(),
        )
    }
    // 10件登録できること。フィルター条件で抽出し所持数の昇順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndSelectWithQueryOrderByHavingNumberAsc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }.sortedBy { it.havingNumber },
            yarnDataDAO.selectWithQuery("101","${SortKey.YARN_HAVING_NUMBER.rowValue} ${SortOrder.ASC.rowValue}").first(),
        )
    }
    // 10件登録できること。フィルター条件で抽出し所持数の降順で並び替えられること。
    @Test
    @Throws(Exception::class)
    fun yarnDataDAOTest_daoInsert10Items_insertsItemsIntoDBAndSelectWithQueryOrderByHavingNumberDesc() = runBlocking {
        for(yarnData in YarnDummyData.dummyDataList)
            yarnDataDAO.insert(yarnData)

        assertEquals(
            YarnDummyData.dummyDataList.filter { it.yarnName.contains("101") }.sortedByDescending { it.havingNumber },
            yarnDataDAO.selectWithQuery("101","${SortKey.YARN_HAVING_NUMBER.rowValue} ${SortOrder.DESC.rowValue}").first(),
        )
    }
}

object YarnDummyData {
    val dummyDataList =
        listOf(
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