package jp.hibeiko.yarnshelf.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import jp.hibeiko.yarnshelf.data.DataSource
import jp.hibeiko.yarnshelf.data.YarnData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

private const val TAG = "HomeScreen"
data class HomeScreenUiState(
    val yarnDataList: List<YarnData> = DataSource().loadData(),
    val yarnEditData: YarnData = YarnData("","", Date(),0),
    val cardClickedFlag: Boolean = false,
    val cardClickedYarnNumber: Int = 0,
)
class HomeScreenViewModel: ViewModel() {
    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    //_home...を直接publicにしてしまうと外部からset可能となるため、home...をpublicにして読み取り専用としてasStateFlow()経由で利用するように制御する。
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    fun cardOnClick(yarnName: String){
//        Log.d(TAG,"yarnName = $yarnName, ${_homeScreenUiState.value}")
        _homeScreenUiState.update { it ->
            it.copy(
                cardClickedFlag = !it.cardClickedFlag,
                cardClickedYarnNumber = it.yarnDataList.indexOf(it.yarnDataList.first { it.yarnName == yarnName }),
                yarnEditData = it.yarnDataList.first { it.yarnName == yarnName }
            )
        }
//        Log.d(TAG,"yarnName = $yarnName, ${_homeScreenUiState.value}")
    }

    fun dialogOnClick(){
//        Log.d(TAG,"${_homeScreenUiState.value}")
        _homeScreenUiState.update { currentState ->
            currentState.copy(
                cardClickedFlag = !currentState.cardClickedFlag,
            )
        }
//        Log.d(TAG,"${_homeScreenUiState.value}")
    }

    fun yarnNameUpdate(yarnName: String){
        _homeScreenUiState.update {
            it.copy(
                yarnEditData = YarnData(yarnName,it.yarnEditData.yarnDescription,it.yarnEditData.lastUpdateDate,it.yarnEditData.drawableResourceId)
            )
        }
    }
    fun yarnDescriptionUpdate(yarnDescription: String){
        _homeScreenUiState.update {
            it.copy(
                yarnEditData = YarnData(it.yarnEditData.yarnName,yarnDescription,it.yarnEditData.lastUpdateDate,it.yarnEditData.drawableResourceId)
            )
        }
    }
    fun yarnLastUpdateDateUpdate(lastUpdateDate: Date){
        _homeScreenUiState.update {
            it.copy(
                yarnEditData = YarnData(it.yarnEditData.yarnName,it.yarnEditData.yarnDescription,lastUpdateDate,it.yarnEditData.drawableResourceId)
            )
        }
    }
}