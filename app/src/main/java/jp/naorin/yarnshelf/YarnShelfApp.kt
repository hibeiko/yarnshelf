package jp.naorin.yarnshelf

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import jp.naorin.yarnshelf.ui.navigation.YarnShelfNavHost

@Preview
@Composable
fun YarnShelfApp(
    navController: NavHostController = rememberNavController()
) {
    YarnShelfNavHost(navController = navController)
}
