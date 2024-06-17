package jp.hibeiko.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.launch
import java.util.Date

//private const val TAG = "HomeScreen"

data class YarnConfirmScreenUiState(
    val yarnConfirmData: YarnData = YarnData()
)

class YarnConfirmScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository
) : ViewModel() {
    // 前画面からのリクエストパラメータ
    private val yarnId: Int = checkNotNull(savedStateHandle[YarnConfirmDestination.yarnIdArg])
    private val yarnName: String =
        checkNotNull(savedStateHandle[YarnConfirmDestination.yarnNameArg])
    private val yarnDescription: String =
        checkNotNull(savedStateHandle[YarnConfirmDestination.yarnDescriptionArg])
    private val janCode: String = checkNotNull(savedStateHandle[YarnConfirmDestination.janCodeArg])
    private val imageUrl: String =
        checkNotNull(savedStateHandle[YarnConfirmDestination.imageUrlArg])
    private val drawableResourceId: Int =
        checkNotNull(savedStateHandle[YarnConfirmDestination.drawableResourceIdArg])
    var yarnConfirmScreenUiState by mutableStateOf(
        YarnConfirmScreenUiState(
            YarnData(
                yarnId,
                janCode,
                yarnName,
                yarnDescription,
                Date(),
                imageUrl,
                drawableResourceId
            )
        )
    )
        private set

    fun updateYarnData() {
        if (validateInput()) {
            viewModelScope.launch {
                yarnDataRepository.update(yarnConfirmScreenUiState.yarnConfirmData)
            }
        }
    }

    private fun validateInput(): Boolean {
        return with(yarnConfirmScreenUiState) {
            this.yarnConfirmData.yarnName.isNotBlank() && this.yarnConfirmData.yarnDescription.isNotBlank()
        }
    }

}