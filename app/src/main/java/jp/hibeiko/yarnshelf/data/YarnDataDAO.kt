package jp.hibeiko.yarnshelf.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface YarnDataDAO {
    // 引数 onConflict は、競合が発生した場合の処理を Room に伝えます。OnConflictStrategy.IGNORE 戦略は、新しいアイテムを無視します。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(yarnData: YarnData)

    @Update
    suspend fun update(yarnData: YarnData)

    @Delete
    suspend fun delete(yarnData: YarnData)

    // 永続化レイヤでは Flow を使用することをおすすめします。戻り値の型として Flow を指定すると、データベース内のデータが変更されるたびに通知が届きます。Room がこの Flow を最新の状態に維持します。つまり、データを明示的に取得する必要があるのは一度だけです。このセットアップは次の Codelab で実装する在庫リストを更新する際に役立ちます。戻り値の型が Flow であるため、Room はバックグラウンド スレッドでクエリを実行します。明示的に suspend 関数にしてコルーチン スコープ内で呼び出す必要はありません。
    @Query("SELECT * FROM yarndata WHERE yarnId = :yarnId")
    fun select(yarnId: Int) : Flow<YarnData>

    @Query("SELECT * FROM yarndata ORDER BY yarnName ASC")
    fun selectAll() : Flow<List<YarnData>>
}