package com.example.projetodirigido.ui.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetodirigido.model.LauncherApp
import com.example.projetodirigido.ui.components.AppTile
import com.example.projetodirigido.data.AppIconLoader
import androidx.compose.foundation.lazy.grid.rememberLazyGridState


@Composable
fun AppsScreen(
    apps: List<LauncherApp>,
    iconLoader: AppIconLoader,
    fontScale: Float,
    onOpenApp: (LauncherApp) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(
            minSize = 140.dp
        ),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = apps,
            key = { app ->
                app.key
            },
            contentType = {
                "launcher-app"
            }
        ) { app ->
            AppTile(
                app = app,
                iconLoader = iconLoader,
                fontScale = fontScale,
                onClick = {
                    onOpenApp(app)
                }
            )
        }
    }
}