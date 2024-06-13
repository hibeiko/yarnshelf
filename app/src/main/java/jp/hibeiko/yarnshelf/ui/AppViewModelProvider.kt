package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import jp.hibeiko.yarnshelf.YarnShelfApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Home
        initializer {
            HomeScreenViewModel(yarnShelfApplication().container.yarnDataRepository)
        }
        // 毛糸情報登録画面
        initializer {
            YarnEntryScreenViewModel(yarnShelfApplication().container.yarnDataRepository)
        }
        // 毛糸情報詳細画面
        initializer {
            YarnDetailScreenViewModel(
                this.createSavedStateHandle(),
                yarnShelfApplication().container.yarnDataRepository
            )
        }
        // 毛糸情報編集画面
        initializer {
            YarnEditScreenViewModel(
                this.createSavedStateHandle(),
                yarnShelfApplication().container.yarnDataRepository
            )
        }
        // 毛糸情報確認画面
        initializer {
            YarnConfirmScreenViewModel(
                this.createSavedStateHandle(),
                yarnShelfApplication().container.yarnDataRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.yarnShelfApplication(): YarnShelfApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YarnShelfApplication)