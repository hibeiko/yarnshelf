package jp.hibeiko.yarnshelf


import android.app.Application
import jp.hibeiko.yarnshelf.data.AppContainer
import jp.hibeiko.yarnshelf.data.AppDataContainer

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