package jp.hibeiko.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.MLKitRepository
import jp.hibeiko.yarnshelf.repository.YahooShoppingWebServiceItemSearchApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//private const val TAG = "HomeScreen"

data class ItemSearchScreenUiState(
    val itemSearchData: YarnData = YarnData(),
    val yarnNameSearchInput: String = "",
    val yarnJanCodeSearchInput: String = "4981769205126",
    val selectedTabIndex: Int = 0
)

// API Callの状態管理
sealed interface SearchItemUiState {
    data class Success(val responseItem: YahooShoppingWebServiceItemData) : SearchItemUiState
    object Error : SearchItemUiState
    object Loading : SearchItemUiState
}

class ItemSearchScreenViewModel(
    private val yahooShoppingWebServiceItemSearchApiRepository: YahooShoppingWebServiceItemSearchApiRepository,
    private val mlKitRepository: MLKitRepository
) : ViewModel() {
    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
    private val _itemSearchScreenUiState = MutableStateFlow(ItemSearchScreenUiState())

    //_home...を直接publicにしてしまうと外部からset可能となるため、home...をpublicにして読み取り専用としてasStateFlow()経由で利用するように制御する。
    val itemSearchScreenUiState: StateFlow<ItemSearchScreenUiState> =
        _itemSearchScreenUiState.asStateFlow()

    // APIの戻り値表示用文字列
    var searchItemUiState: SearchItemUiState by mutableStateOf(SearchItemUiState.Loading)
        private set

    fun yarnNameSearchInputUpdate(yarnNameSearchInput: String) {
        _itemSearchScreenUiState.update { it.copy(yarnNameSearchInput = yarnNameSearchInput) }
    }

    fun selectedTabIndexUpdate(selectedTabIndex: Int) {
        _itemSearchScreenUiState.update {
            it.copy(
                selectedTabIndex = selectedTabIndex,
                yarnNameSearchInput = ""
            )
        }
        searchItemUiState = SearchItemUiState.Loading
    }

    fun validateYarnNameSearchInput(): Boolean {
        return with(_itemSearchScreenUiState) {
            this.value.yarnNameSearchInput.isNotBlank()
        }
    }

    fun searchItem(yarnName: String, janCode: String) {
        if (janCode.isNotBlank() || validateYarnNameSearchInput()) {
            viewModelScope.launch {
                try {
                    val tempResponse =
                        yahooShoppingWebServiceItemSearchApiRepository.searchItem(
                            query = yarnName,
                            janCode = janCode
                        )
                    searchItemUiState = SearchItemUiState.Success(tempResponse)
                } catch (e: Exception) {
                    e.printStackTrace()
                    searchItemUiState = SearchItemUiState.Error
                }
            }
        }
    }

    fun readBarcode() {
        mlKitRepository.getJanCode(::searchItem)
    }
}
