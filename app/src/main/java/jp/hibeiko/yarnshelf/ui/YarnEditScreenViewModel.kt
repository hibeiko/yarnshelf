package jp.hibeiko.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.common.YarnParamName
import jp.hibeiko.yarnshelf.common.updateYarnData
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//private const val TAG = "HomeScreen"
data class YarnEditScreenUiState(
    val yarnEditData: YarnData = YarnData()
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
                yarnDataRepository.select(yarnId)
                    .filterNotNull()
                    .first()
            )
        }
    }

    fun updateYarnEditData(param: Any, paramName: YarnParamName) {
        yarnEditScreenUiState = yarnEditScreenUiState.copy(
            yarnEditData = updateYarnData(param = param, yarnData = yarnEditScreenUiState.yarnEditData, paramName = paramName)
        )
    }

    fun validateInput(): Boolean {
        return with(yarnEditScreenUiState) {
            this.yarnEditData.yarnName.isNotBlank() && this.yarnEditData.yarnDescription.isNotBlank()
        }
    }

}

