package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//private const val TAG = "YarnDetailScreenViewModel"

data class YarnDetailScreenUiState(
    val yarnDetailData: YarnData = YarnData()
)

class YarnDetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository
) : ViewModel() {
    // 前画面からのリクエストパラメータ
    private val yarnId: Int = checkNotNull(savedStateHandle[YarnDetailDestination.yarnIdArg])

    // ダイアログ表示非表示切替フラグ(デフォルトはfalse(非表示))
    val dialogViewFlag = MutableStateFlow(false)

    val yarnDetailScreenUiState: StateFlow<YarnDetailScreenUiState> =
        yarnDataRepository.select(yarnId)
            .filterNotNull()
            .map {
                YarnDetailScreenUiState(yarnDetailData = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = YarnDetailScreenUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun deleteYarnData() {
        dialogViewFlag.update { false }
        viewModelScope.launch {
            yarnDataRepository.delete(yarnData = yarnDetailScreenUiState.value.yarnDetailData)
        }
    }
    fun updateDialogViewFlag(flag : Boolean) {
        dialogViewFlag.update { flag }
    }
}
