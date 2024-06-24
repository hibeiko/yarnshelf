package jp.hibeiko.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.common.yarnDataForScreenToYarnDataConverter
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import kotlinx.coroutines.launch

data class YarnConfirmScreenUiState(
    val yarnConfirmData: YarnData = YarnData()
)

class YarnConfirmScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository
) : ViewModel() {
    // 前画面からのリクエストパラメータ
    private val entryItem: YarnDataForScreen =
        checkNotNull(savedStateHandle[YarnConfirmDestination.entryItemArg])
    var yarnConfirmScreenUiState by mutableStateOf(
        YarnConfirmScreenUiState(yarnDataForScreenToYarnDataConverter(entryItem))
    )
        private set

    fun updateYarnData() {
//        if (validateInput()) {
            viewModelScope.launch {
//                yarnDataRepository.update(yarnConfirmScreenUiState.yarnConfirmData)
                yarnDataRepository.insert(yarnData = yarnConfirmScreenUiState.yarnConfirmData)
            }
//        }
    }

//    private fun validateInput(): Boolean {
//        return with(yarnConfirmScreenUiState) {
//            this.yarnConfirmData.yarnName.isNotBlank() && this.yarnConfirmData.yarnDescription.isNotBlank()
//        }
//    }

}