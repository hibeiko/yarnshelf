package jp.hibeiko.yarnshelf.repository

import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.data.YarnDataDAO
import kotlinx.coroutines.flow.Flow

interface YarnDataRepository {
    // 引数 onConflict は、競合が発生した場合の処理を Room に伝えます。OnConflictStrategy.IGNORE 戦略は、新しいアイテムを無視します。
    suspend fun insert(yarnData: YarnData)
    suspend fun update(yarnData: YarnData)
    suspend fun delete(yarnData: YarnData)
    fun select(yarnId: Int) : Flow<YarnData?>
    fun selectAll() : Flow<List<YarnData>>
}

class YarnDataRepositoryImpl(private val yarnDataDAO: YarnDataDAO) : YarnDataRepository {
    override suspend fun insert(yarnData: YarnData) = yarnDataDAO.insert(yarnData)
    override suspend fun update(yarnData: YarnData) = yarnDataDAO.update(yarnData)
    override suspend fun delete(yarnData: YarnData) = yarnDataDAO.delete(yarnData)
    override fun select(id: Int): Flow<YarnData?> = yarnDataDAO.select(id)
    override fun selectAll(): Flow<List<YarnData>> = yarnDataDAO.selectAll()

}