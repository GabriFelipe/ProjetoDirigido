package com.example.projetodirigido.model

import android.content.ComponentName
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class LauncherApp(
    val label: String,
    val packageName: String,
    val componentName: ComponentName,
    val icon: Bitmap,
    val backgroundColor: Color
)