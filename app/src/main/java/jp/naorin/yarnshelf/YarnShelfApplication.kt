package jp.naorin.yarnshelf


import android.app.Application
import jp.naorin.yarnshelf.repository.AppContainer
import jp.naorin.yarnshelf.repository.AppDataContainer

class YarnShelfApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}