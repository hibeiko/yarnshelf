package jp.naorin.yarnshelf.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import jp.naorin.yarnshelf.YarnShelfApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Home
        initializer {
            HomeScreenViewModel(yarnShelfApplication().container.yarnDataRepository)
        }
        // 商品検索画面
        initializer {
            ItemSearchScreenViewModel(
                yarnShelfApplication().container.yahooShoppingWebServiceItemSearchApiRepository,
                yarnShelfApplication().container.mlKitRepository,
                yarnShelfApplication().container.yarnDataRepository
            )
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
    }
}

/**
 * Extension function to queries for [YarnShelfApplication] object and returns an instance of
 * [YarnShelfApplication].
 */
fun CreationExtras.yarnShelfApplication(): YarnShelfApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YarnShelfApplication)
// コンパニオン オブジェクトは、すべてのユーザーが使用するオブジェクトのインスタンスを 1 つ使用できる点で便利です。高価なオブジェクトのインスタンスを新たに作成する必要はありません。これは実装の詳細であり、分離することで、アプリのコードの他の部分に影響を与えずに変更を加えることができます。
//
//APPLICATION_KEY は ViewModelProvider.AndroidViewModelFactory.Companion オブジェクトの一部で、アプリの MarsPhotosApplication オブジェクトの検出に使用されます。このオブジェクトには、依存関係インジェクションに使用されるリポジトリを取得するための container プロパティがあります。
