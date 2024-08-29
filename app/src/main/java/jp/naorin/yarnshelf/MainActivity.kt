package jp.naorin.yarnshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import jp.naorin.yarnshelf.ui.theme.YarnShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YarnShelfTheme {
                YarnShelfApp()
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun YarnShelfPreview() {
    YarnShelfTheme {
        YarnShelfApp()
    }
}