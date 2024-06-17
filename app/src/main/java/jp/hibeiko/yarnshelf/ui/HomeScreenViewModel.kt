package jp.hibeiko.yarnshelf.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

private const val TAG = "HomeScreen"

data class HomeScreenUiState(
    val yarnDataList: List<YarnData> = listOf() // DataSource().loadData(),
)

class HomeScreenViewModel(private val yarnDataRepository: YarnDataRepository) : ViewModel() {
    //StateFlow は、現在の状態や新しい状態更新の情報を出力するデータ保持用の監視可能な Flow です。その value プロパティは、現在の状態値を反映します。状態を更新してこの Flow に送信するには、MutableStateFlow クラスの value プロパティに新しい値を割り当てます。
//    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    //_home...を直接publicにしてしまうと外部からset可能となるため、home...をpublicにして読み取り専用としてasStateFlow()経由で利用するように制御する。
//    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()
    val homeScreenUiState: StateFlow<HomeScreenUiState> =
        yarnDataRepository.selectAll().map { HomeScreenUiState(yarnDataList = it) }
            .stateIn(
                // scope - viewModelScope は、StateFlow のライフサイクルを定義します。viewModelScope がキャンセルされると、StateFlow もキャンセルされます。
                scope = viewModelScope,
                // started - パイプラインは、UI が表示されている場合にのみアクティブにする必要があります。そのためには SharingStarted.WhileSubscribed() を使用します。最後のサブスクライバーの消失から共有コルーチンの停止までの遅延（ミリ秒単位）を設定するには、TIMEOUT_MILLIS を SharingStarted.WhileSubscribed() メソッドに渡します。
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeScreenUiState()
            )

    //    val yarnEditData: YarnData = YarnData(0,"","", Date(),0),
    // ダイアログ表示非表示切替フラグ(デフォルトはfalse(非表示))
    val dialogViewFlag = MutableStateFlow(false)

    // ダイアログに表示するYarnDataのyarnId(デフォルトは0)
    val dialogViewYarnId = MutableStateFlow(0)

    //
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun cardOnClick(yarnId: Int) {
//        Log.d(TAG," ${DataSource().loadData()},")
//        Log.d(TAG,"yarnName = $yarnName, ${_homeScreenUiState.value}")
        dialogViewFlag.update { !it }
        dialogViewYarnId.update { yarnId }
    }

    fun dialogOnClick() {
        dialogViewFlag.update { !it }
//        Log.d(TAG,"${_homeScreenUiState.value}")
//        _homeScreenUiState.update { currentState ->
//            currentState.copy(
//                cardClickedFlag = !currentState.cardClickedFlag,
//            )
//        }
//        Log.d(TAG,"${_homeScreenUiState.value}")
    }

}