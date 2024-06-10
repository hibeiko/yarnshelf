package jp.hibeiko.yarnshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.hibeiko.yarnshelf.ui.HomeScreen
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YarnShelfTheme {
                HomeScreen()
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun YarnShelfPreview() {
    YarnShelfTheme {
        HomeScreen()
    }
}