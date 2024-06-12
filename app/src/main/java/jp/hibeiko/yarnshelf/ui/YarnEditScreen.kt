package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YarnEditScreen(
    homeScreenUiState: HomeScreenUiState,
    yarnNameOnValueChange: (String) -> Unit,
    yarnDescriptionOnValueChange: (String) -> Unit,
    nextButtonOnClick: () -> Unit,
    cancelButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TextField(
            label = { Text("名前", style = MaterialTheme.typography.labelSmall)},
            leadingIcon = {Icon(Icons.Default.Edit, contentDescription = null)},
            value = homeScreenUiState.yarnEditData.yarnName,
            onValueChange = {yarnNameOnValueChange(it)},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium
        )
        TextField(
            label = { Text("メモ", style = MaterialTheme.typography.labelSmall)},
            leadingIcon = {Icon(Icons.Default.Edit, contentDescription = null)},
            value = homeScreenUiState.yarnEditData.yarnDescription,
            onValueChange = {yarnDescriptionOnValueChange((it))},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.displayMedium,
            singleLine = false,
            modifier = Modifier.padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.weight(1.0F))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = cancelButtonOnClick
            ) {
                Text(text = "Cancel", style = MaterialTheme.typography.labelSmall)
            }
            Button(onClick = nextButtonOnClick,) {
                Text(text = "Next", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview
@Composable
fun YarnEditScreenPreview() {
    YarnShelfTheme {
        YarnEditScreen(
            homeScreenUiState = HomeScreenUiState(listOf(),YarnData("Seabright","1010 Seabright", Date(), R.drawable.spin_1010_crpd_1625196651766_400),true,0),
            yarnNameOnValueChange = {},
            yarnDescriptionOnValueChange = {},
            nextButtonOnClick = {},
            cancelButtonOnClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}