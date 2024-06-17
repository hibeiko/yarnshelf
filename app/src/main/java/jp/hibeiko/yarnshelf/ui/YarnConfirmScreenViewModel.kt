package jp.hibeiko.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import java.util.Date

private const val TAG = "HomeScreen"
data class YarnConfirmScreenUiState(
    val yarnConfirmData: YarnData = YarnData(0,"","","", Date(),"",0) // DataSource().loadData().first { it.yarnId == 0 }
    )
class YarnConfirmScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository
): ViewModel() {
    // 前画面からのリクエストパラメータ
    private val yarnId: Int = checkNotNull( savedStateHandle[YarnConfirmDestination.yarnIdArg])
    private val yarnName: String = checkNotNull( savedStateHandle[YarnConfirmDestination.yarnNameArg])
    private val yarnDescription: String = checkNotNull( savedStateHandle[YarnConfirmDestination.yarnDescriptionArg])
    private val janCode: String = checkNotNull( savedStateHandle[YarnConfirmDestination.janCodeArg])
    private val imageUrl: String = checkNotNull( savedStateHandle[YarnConfirmDestination.imageUrlArg])
    var yarnConfirmScreenUiState by mutableStateOf(YarnConfirmScreenUiState(YarnData(yarnId, janCode,yarnName,yarnDescription,Date(),imageUrl,0)))
        private set

    suspend fun updateYarnData(yarnConfirmData: YarnData) {
        if(validateInput()) {
            yarnDataRepository.update(yarnConfirmData)
        }
    }

    fun validateInput(): Boolean {
        return with(yarnConfirmScreenUiState) {
            this.yarnConfirmData.yarnName.isNotBlank() && this.yarnConfirmData.yarnDescription.isNotBlank()
        }
    }

}