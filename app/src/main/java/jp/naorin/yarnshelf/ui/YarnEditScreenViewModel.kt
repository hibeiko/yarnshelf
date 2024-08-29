package jp.naorin.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.naorin.yarnshelf.common.YarnParamName
import jp.naorin.yarnshelf.common.updateYarnData
import jp.naorin.yarnshelf.common.validateInput
import jp.naorin.yarnshelf.common.yarnDataForScreenToYarnDataConverter
import jp.naorin.yarnshelf.common.yarnDataToYarnDataForScreenConverter
import jp.naorin.yarnshelf.repository.YarnDataRepository
import jp.naorin.yarnshelf.ui.navigation.YarnDataForScreen
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//private const val TAG = "HomeScreen"
data class YarnEditScreenUiState(
    val yarnEditData: YarnDataForScreen = YarnDataForScreen(),
    val isErrorMap: MutableMap<YarnParamName, String> = mutableMapOf(),
    val confirmDialogViewFlag: Boolean = false,
)

class YarnEditScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository
) : ViewModel() {
    // 前画面からのリクエストパラメータ
    private val yarnId: Int = checkNotNull(savedStateHandle[YarnEditDestination.yarnIdArg])
    var yarnEditScreenUiState by mutableStateOf(YarnEditScreenUiState())
        private set

    init {
        viewModelScope.launch {
            yarnEditScreenUiState = YarnEditScreenUiState(
                yarnDataToYarnDataForScreenConverter( yarnDataRepository.select(yarnId)
                    .filterNotNull()
                    .first()
                )
            )
            for (paramName in YarnParamName.entries){
                updateIsErrorMap(paramName)
            }
        }
    }

    fun updateYarnEditData(param: Any, paramName: YarnParamName) {
        yarnEditScreenUiState = yarnEditScreenUiState.copy(
            yarnEditData = updateYarnData(
                param = param,
                yarnData = yarnEditScreenUiState.yarnEditData,
                paramName = paramName
            )
        )
        updateIsErrorMap(paramName)
    }

    private fun updateIsErrorMap(paramName: YarnParamName) {
        val errorMessage = validateInput(yarnEditScreenUiState.yarnEditData, paramName)
        // バリデーションエラーがあればメッセージが返却される。エラーがなければ空文字が返却される。
        if (errorMessage.isNotBlank())
            yarnEditScreenUiState.isErrorMap[paramName] = errorMessage
        else
            yarnEditScreenUiState.isErrorMap.remove(paramName)
    }
    fun updateDialogViewFlag(flag : Boolean) {
        yarnEditScreenUiState = yarnEditScreenUiState.copy(confirmDialogViewFlag = flag)
    }
    fun updateYarnData() {
        updateDialogViewFlag(false)
//        if (validateInput()) {
        viewModelScope.launch {
            yarnDataRepository.insert(yarnData = yarnDataForScreenToYarnDataConverter( yarnEditScreenUiState.yarnEditData ))
//                yarnDataRepository.update(yarnConfirmScreenUiState.yarnConfirmData)
        }
//        }
    }
}

