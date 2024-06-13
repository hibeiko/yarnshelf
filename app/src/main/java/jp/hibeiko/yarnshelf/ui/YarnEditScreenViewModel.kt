package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.ViewModel
import jp.hibeiko.yarnshelf.data.YarnData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

private const val TAG = "HomeScreen"
data class YarnEditScreenUiState(
    val yarnEditData: YarnData = YarnData(0,"","", Date(),0) // DataSource().loadData().first { it.yarnId == 0 }
    )
class YarnEditScreenViewModel: ViewModel() {
    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
    private val _yarnEditScreenUiState = MutableStateFlow(YarnEditScreenUiState())
    //_home...を直接publicにしてしまうと外部からset可能となるため、home...をpublicにして読み取り専用としてasStateFlow()経由で利用するように制御する。
    val yarnEditScreenUiState: StateFlow<YarnEditScreenUiState> = _yarnEditScreenUiState.asStateFlow()

    fun yarnNameUpdate(yarnName: String){
        _yarnEditScreenUiState.update {
            it.copy(
                yarnEditData = YarnData(it.yarnEditData.yarnId,yarnName,it.yarnEditData.yarnDescription,it.yarnEditData.lastUpdateDate,it.yarnEditData.drawableResourceId)
            )
        }
    }
    fun yarnDescriptionUpdate(yarnDescription: String){
        _yarnEditScreenUiState.update {
            it.copy(
                yarnEditData = YarnData(it.yarnEditData.yarnId,it.yarnEditData.yarnName,yarnDescription,it.yarnEditData.lastUpdateDate,it.yarnEditData.drawableResourceId)
            )
        }
    }
    fun yarnLastUpdateDateUpdate(lastUpdateDate: Date){
        _yarnEditScreenUiState.update {
            it.copy(
                yarnEditData = YarnData(it.yarnEditData.yarnId,it.yarnEditData.yarnName,it.yarnEditData.yarnDescription,lastUpdateDate,it.yarnEditData.drawableResourceId)
            )
        }
    }

}