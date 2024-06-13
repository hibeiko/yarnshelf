package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.ViewModel
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.data.YarnDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

private const val TAG = "HomeScreen"

data class YarnEntryScreenUiState(
    val yarnEntryData: YarnData = YarnData(0, "", "", Date(), 0)

)

class YarnEntryScreenViewModel(private val yarnDataRepository: YarnDataRepository) : ViewModel() {
    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
    private val _yarnEntryScreenUiState = MutableStateFlow(YarnEntryScreenUiState())

    //_home...を直接publicにしてしまうと外部からset可能となるため、home...をpublicにして読み取り専用としてasStateFlow()経由で利用するように制御する。
    val yarnEntryScreenUiState: StateFlow<YarnEntryScreenUiState> =
        _yarnEntryScreenUiState.asStateFlow()

    fun yarnNameUpdate(yarnName: String) {
        _yarnEntryScreenUiState.update {
            it.copy(
                yarnEntryData = YarnData(
                    it.yarnEntryData.yarnId,
                    yarnName,
                    it.yarnEntryData.yarnDescription,
                    it.yarnEntryData.lastUpdateDate,
                    it.yarnEntryData.drawableResourceId
                )
            )
        }
    }

    fun yarnDescriptionUpdate(yarnDescription: String) {
        _yarnEntryScreenUiState.update {
            it.copy(
                yarnEntryData = YarnData(
                    it.yarnEntryData.yarnId,
                    it.yarnEntryData.yarnName,
                    yarnDescription,
                    it.yarnEntryData.lastUpdateDate,
                    it.yarnEntryData.drawableResourceId
                )
            )
        }
    }

    fun yarnLastUpdateDateUpdate(lastUpdateDate: Date) {
        _yarnEntryScreenUiState.update {
            it.copy(
                yarnEntryData = YarnData(
                    it.yarnEntryData.yarnId,
                    it.yarnEntryData.yarnName,
                    it.yarnEntryData.yarnDescription,
                    lastUpdateDate,
                    it.yarnEntryData.drawableResourceId
                )
            )
        }
    }

    suspend fun saveYarnData() {
        if(validateInput()) {
            yarnDataRepository.insert(yarnData = yarnEntryScreenUiState.value.yarnEntryData)
        }
    }

    fun validateInput(): Boolean {
        return with(yarnEntryScreenUiState) {
            this.value.yarnEntryData.yarnName.isNotBlank() && this.value.yarnEntryData.yarnDescription.isNotBlank()
        }
    }

}
