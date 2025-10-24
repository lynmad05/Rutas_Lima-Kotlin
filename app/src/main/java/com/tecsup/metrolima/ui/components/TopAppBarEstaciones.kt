package com.tecsup.metrolima.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarEstaciones(
    title: String,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTopAppEstaciones() {
    MetroLimaGoTheme {
        // 👇 Ejemplo usando stringResource
        TopAppBarEstaciones(title = stringResource(id = R.string.estaciones_title))
    }
}
