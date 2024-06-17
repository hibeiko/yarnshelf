package jp.hibeiko.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.data.YahooShoppingWebServiceItemData
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YahooShoppingWebServiceItemSearchApiRepository
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

//private const val TAG = "HomeScreen"

data class YarnEntryScreenUiState(
    val yarnEntryData: YarnData = YarnData()

)

// API Callの状態管理
sealed interface SearchItemUiState {
    data class Success(val responseItem: YahooShoppingWebServiceItemData) : SearchItemUiState
    object Error : SearchItemUiState
    object Loading : SearchItemUiState
}

class YarnEntryScreenViewModel(
    private val yarnDataRepository: YarnDataRepository,
    private val yahooShoppingWebServiceItemSearchApiRepository: YahooShoppingWebServiceItemSearchApiRepository
) : ViewModel() {
    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
    private val _yarnEntryScreenUiState = MutableStateFlow(YarnEntryScreenUiState())

    //_home...を直接publicにしてしまうと外部からset可能となるため、home...をpublicにして読み取り専用としてasStateFlow()経由で利用するように制御する。
    val yarnEntryScreenUiState: StateFlow<YarnEntryScreenUiState> =
        _yarnEntryScreenUiState.asStateFlow()

    // APIの戻り値表示用文字列
    var searchItemUiState: SearchItemUiState by mutableStateOf(SearchItemUiState.Loading)
        private set

    fun yarnNameUpdate(yarnName: String) {
        _yarnEntryScreenUiState.update {
            it.copy(
                yarnEntryData = it.yarnEntryData.copy(
                    yarnName = yarnName
                )
            )
        }
    }

    fun yarnDescriptionUpdate(yarnDescription: String) {
        _yarnEntryScreenUiState.update {
            it.copy(
                yarnEntryData = it.yarnEntryData.copy(
                    yarnDescription = yarnDescription
                )
            )
        }
    }

    fun yarnJanCodeUpdate(janCode: String) {
        _yarnEntryScreenUiState.update {
            it.copy(
                yarnEntryData = it.yarnEntryData.copy(
                    janCode = janCode
                )
            )
        }
    }


    fun saveYarnData() {
        if (validateInput()) {
            viewModelScope.launch {
                yarnDataRepository.insert(yarnData = yarnEntryScreenUiState.value.yarnEntryData)
            }
        }
    }

    fun validateInput(): Boolean {
        return with(yarnEntryScreenUiState) {
            this.value.yarnEntryData.yarnName.isNotBlank() && this.value.yarnEntryData.yarnDescription.isNotBlank()
        }
    }

    fun validateJanCodeInput(): Boolean {
        return with(yarnEntryScreenUiState) {
            this.value.yarnEntryData.janCode.isNotBlank()
        }
    }

    fun searchItem(janCode: String) {
        if (validateJanCodeInput()) {
            try {
                viewModelScope.launch {
                    val tempResponse =
                        yahooShoppingWebServiceItemSearchApiRepository.searchItem(janCode = janCode)
                    tempResponse.hits.first().let { tempItem ->
                        _yarnEntryScreenUiState.update {
                            it.copy(
                                yarnEntryData = YarnData(
                                    yarnName = tempItem.name ?: "",
                                    yarnDescription = tempItem.description ?: "",
                                    janCode = tempItem.janCode ?: "",
                                    imageUrl = tempItem.image?.medium ?: "",
                                    lastUpdateDate = Date(),
                                    drawableResourceId = 0
                                )
                            )
                        }
                    }
                    searchItemUiState = SearchItemUiState.Success(tempResponse)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                searchItemUiState = SearchItemUiState.Error
            }
        }
    }
}
