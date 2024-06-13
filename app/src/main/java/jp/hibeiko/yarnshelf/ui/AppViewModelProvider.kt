package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import jp.hibeiko.yarnshelf.YarnShelfApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
//        initializer {
//            YarnEditScreenViewModel(
//                this.createSavedStateHandle(),
//                yarnShelfApplication().container.yarnDataRepository
//            )
//        }
        // Initializer for ItemEntryViewModel
        initializer {
            YarnEntryScreenViewModel(yarnShelfApplication().container.yarnDataRepository)
        }

//         Initializer for ItemDetailsViewModel
//        initializer {
//            ItemDetailsViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.itemsRepository
//            )
//        }

        // Initializer for HomeViewModel
        initializer {
            HomeScreenViewModel(yarnShelfApplication().container.yarnDataRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.yarnShelfApplication(): YarnShelfApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YarnShelfApplication)