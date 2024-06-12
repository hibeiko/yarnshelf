package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import jp.hibeiko.yarnshelf.data.YarnData
import java.text.SimpleDateFormat

@Composable
fun HomeScreen(
    homeScreenUiState: HomeScreenUiState,
    cardOnClick: (String) -> Unit,
    dialogOnClick: () -> Unit,
    onEditButtonClicked: () -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn( modifier = modifier) {
        items(homeScreenUiState.yarnDataList) {
            YarnCard(
                it,
                cardOnClick = cardOnClick,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
    if( homeScreenUiState.cardClickedFlag ){
        YarnDialog(
            homeScreenUiState.yarnDataList[homeScreenUiState.cardClickedYarnNumber],
            dialogOnClick,
            onEditButtonClicked,
        )
    }
}

@Composable
fun YarnCard(
    yarnData: YarnData,
    cardOnClick: (String) -> Unit,
    modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
//            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer) ,
        onClick = { cardOnClick(yarnData.yarnName) },
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
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
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

@Composable
fun YarnDialog(
    yarnData: YarnData,
    dialogOnClick: () -> Unit,
    onEditButtonClicked: () -> Unit,
    modifier: Modifier = Modifier){
    AlertDialog(
        onDismissRequest = {
            dialogOnClick()
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        title = {
            Text(
                text = yarnData.yarnName,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayLarge
            ) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(yarnData.drawableResourceId),
                    contentDescription = null,
//                modifier = Modifier.padding(10.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = yarnData.yarnDescription,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(2.dp)
                    )
            }
               },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    onEditButtonClicked()
                }
            ) {
                Text(text ="編集")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogOnClick()
                }
            ) {
                Text(text ="OK")
            }
        }
    )

}
