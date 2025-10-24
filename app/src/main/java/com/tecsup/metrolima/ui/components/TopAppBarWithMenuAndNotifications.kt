package com.tecsup.metrolima.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenuAndNotifications(
    title: String = stringResource(R.string.app_name), // 🔤 Usa traducción por defecto
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTopAppBarWithMenuAndNotifications() {
    MetroLimaGoTheme {
        TopAppBarWithMenuAndNotifications(title = stringResource(id = R.string.favoritos_title))
    }
}
