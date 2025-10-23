package com.tecsup.metrolima.ui.components

import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenuAndNotifications(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text("MetroLima GO", style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold))},
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Menú")
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
        TopAppBarWithMenuAndNotifications()
    }
}