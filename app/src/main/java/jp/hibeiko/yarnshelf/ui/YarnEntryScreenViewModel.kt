package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import jp.hibeiko.yarnshelf.common.YarnParamName
import jp.hibeiko.yarnshelf.common.updateYarnData
import jp.hibeiko.yarnshelf.common.validateInput
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//private const val TAG = "HomeScreen"

data class YarnEntryScreenUiState(
    val yarnEntryData: YarnDataForScreen = YarnDataForScreen(),
    val isErrorMap: MutableMap<YarnParamName, String> = mutableMapOf(YarnParamName.YARN_NAME to "名前は必須項目です。"),
)

class YarnEntryScreenViewModel(
    savedStateHandle: SavedStateHandle,
//    private val yarnDataRepository: YarnDataRepository,
) : ViewModel() {

    // 前画面からのリクエストパラメータ
    private val searchItem: YarnDataForScreen =
        checkNotNull(savedStateHandle[YarnEntryDestination.searchItemArg])

    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
    private val _yarnEntryScreenUiState = MutableStateFlow(
        YarnEntryScreenUiState(yarnEntryData = searchItem)
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
        updateIsErrorMap(paramName)
    }

//    fun saveYarnData() {
//        if (validateInput()) {
//            viewModelScope.launch {
//                yarnDataRepository.insert(yarnData = yarnEntryScreenUiState.value.yarnEntryData)
//            }
//        }
//    }

    private fun updateIsErrorMap(paramName: YarnParamName) {
        val errorMessage = validateInput(_yarnEntryScreenUiState.value.yarnEntryData, paramName)
        // バリデーションエラーがあればメッセージが返却される。エラーがなければ空文字が返却される。
        if (errorMessage.isNotBlank())
            _yarnEntryScreenUiState.value.isErrorMap[paramName] = errorMessage
        else
            _yarnEntryScreenUiState.value.isErrorMap.remove(paramName)
    }
}
