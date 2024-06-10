package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.data.DataSource
import jp.hibeiko.yarnshelf.data.YarnData
import java.text.SimpleDateFormat

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
        ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.displayLarge,
                        )
                    }
                )
            }
        ) { innerPadding ->
            HomeScreenBody(
                DataSource().loadData(),
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
fun HomeScreenBody(yarnDataList: List<YarnData>,modifier: Modifier = Modifier) {
    LazyColumn( modifier = modifier) {
        items(yarnDataList) {
            YarnCard(
                it,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun YarnCard(yarnData: YarnData, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
//            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer) ,
        onClick = { /*TODO*/ },
    ) {
        val formatter = SimpleDateFormat("yyyy/MM/dd")
        Row{
            Image(
                painter = painterResource(yarnData.drawableResourceId),
                contentDescription = null,
//                modifier = Modifier.padding(10.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(
                    text = yarnData.yarnName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = yarnData.yarnDescription,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = "最終更新日：${formatter.format(yarnData.lastUpdateDate)}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End),
//                    textAlign = TextAlign.End
                )
            }
        }
    }

}
