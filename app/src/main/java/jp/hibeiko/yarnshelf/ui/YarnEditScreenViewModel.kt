package jp.hibeiko.yarnshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "HomeScreen"
data class YarnEditScreenUiState(
    val yarnEditData: YarnData = YarnData(0,"","", "",Date(),"",0) // DataSource().loadData().first { it.yarnId == 0 }
    )
class YarnEditScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val yarnDataRepository: YarnDataRepository
): ViewModel() {
    // 前画面からのリクエストパラメータ
//    private val yarnId: Int = checkNotNull(savedStateHandle[YarnEditDestination.yarnIdArg])
    private val yarnId: Int = checkNotNull( savedStateHandle[YarnEditDestination.yarnIdArg])
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
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateYarnName(yarnName: String){
        yarnEditScreenUiState = yarnEditScreenUiState.copy(yarnEditScreenUiState.yarnEditData.copy(yarnName = yarnName))
    }
    fun updateYarnDescription(yarnDescription: String){
        yarnEditScreenUiState = yarnEditScreenUiState.copy(yarnEditScreenUiState.yarnEditData.copy(yarnDescription = yarnDescription))
    }
    suspend fun updateYarnData(yarnEditData: YarnData) {
        if(validateInput()) {
            yarnDataRepository.update(yarnEditData)
        }
    }

    fun validateInput(): Boolean {
        return with(yarnEditScreenUiState) {
            this.yarnEditData.yarnName.isNotBlank() && this.yarnEditData.yarnDescription.isNotBlank()
        }
    }

}