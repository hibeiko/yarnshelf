package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.common.yarnDataForScreenToYarnDataConverter
import jp.hibeiko.yarnshelf.common.YarnParamName
import jp.hibeiko.yarnshelf.common.updateYarnData
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//private const val TAG = "HomeScreen"

data class YarnEntryScreenUiState(
    val yarnEntryData: YarnData = YarnData(),
    val validateInputFlag: Boolean = false
)

class YarnEntryScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository,
) : ViewModel() {

    // 前画面からのリクエストパラメータ
    private val searchItem: YarnDataForScreen =
        checkNotNull(savedStateHandle[YarnEntryDestination.searchItemArg])

    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
    private val _yarnEntryScreenUiState = MutableStateFlow(
        YarnEntryScreenUiState(yarnEntryData = yarnDataForScreenToYarnDataConverter(searchItem))
    )

    //_home...を直接publicにしてしまうと外部からset可能となるため、home...をpublicにして読み取り専用としてasStateFlow()経由で利用するように制御する。
    val yarnEntryScreenUiState: StateFlow<YarnEntryScreenUiState> =
        _yarnEntryScreenUiState.asStateFlow()

    fun updateYarnEditData(param: Any, paramName: YarnParamName) {
        _yarnEntryScreenUiState.update{
            it.copy(
                yarnEntryData = updateYarnData(param = param, yarnData = it.yarnEntryData, paramName = paramName)
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
    fun validateInput(param: Boolean) {
        _yarnEntryScreenUiState.update {
            it.copy(validateInputFlag = param)
        }
    }
}
