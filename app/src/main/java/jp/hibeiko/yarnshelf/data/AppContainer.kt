package jp.hibeiko.yarnshelf.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val yarnDataRepository: YarnDataRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val yarnDataRepository: YarnDataRepository by lazy {
        OfflineYarnDataRepository(YarnShelfDatabase.getDatabase(context).yarnDataDAO())
    }
}