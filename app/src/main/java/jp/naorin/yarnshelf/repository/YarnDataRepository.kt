package jp.naorin.yarnshelf.repository

import jp.naorin.yarnshelf.data.YarnData
import jp.naorin.yarnshelf.data.YarnDataDAO
import kotlinx.coroutines.flow.Flow

interface YarnDataRepository {
    suspend fun insert(yarnData: YarnData)
    suspend fun update(yarnData: YarnData)
    suspend fun delete(yarnData: YarnData)
    fun select(yarnId: Int) : Flow<YarnData?>
    fun selectAll(sortStr: String) : Flow<List<YarnData>>
    fun selectWithQuery(query: String,sortStr: String) : Flow<List<YarnData>>
}

class YarnDataRepositoryImpl(private val yarnDataDAO: YarnDataDAO) : YarnDataRepository {
    override suspend fun insert(yarnData: YarnData) = yarnDataDAO.insert(yarnData)
    override suspend fun update(yarnData: YarnData) = yarnDataDAO.update(yarnData)
    override suspend fun delete(yarnData: YarnData) = yarnDataDAO.delete(yarnData)
    override fun select(yarnId: Int): Flow<YarnData?> = yarnDataDAO.select(yarnId)
    override fun selectAll(sortStr: String): Flow<List<YarnData>> = yarnDataDAO.selectAll(sortStr)
    override fun selectWithQuery(query: String, sortStr: String): Flow<List<YarnData>> =
        yarnDataDAO.selectWithQuery(query, sortStr)
}