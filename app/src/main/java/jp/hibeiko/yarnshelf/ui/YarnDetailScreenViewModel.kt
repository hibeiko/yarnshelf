package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.data.YarnDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Date

private const val TAG = "YarnDetailScreenViewModel"

data class YarnDetailScreenUiState(
    val yarnDetailData: YarnData = YarnData(0, "", "", Date(), 0)

)

class YarnDetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository
) : ViewModel() {
    // 前画面からのリクエストパラメータ
    private val yarnId: Int = checkNotNull(savedStateHandle[YarnDetailDestination.yarnIdArg])

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
    suspend fun deleteYarnData() {
        yarnDataRepository.delete(yarnData = yarnDetailScreenUiState.value.yarnDetailData)
    }
}
