package com.example.projetodirigido.model

import android.content.ComponentName
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color

data class LauncherApp(
    val label: String,
    val packageName: String,
    val activityName: String
) {
    val key: String = "$packageName/$activityName"

    fun componentName(): ComponentName {
        return ComponentName(packageName, activityName)
    }
}