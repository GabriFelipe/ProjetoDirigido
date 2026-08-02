package com.example.projetodirigido.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import com.example.projetodirigido.model.LauncherApp
import java.text.Collator
import java.util.Locale

class LauncherAppsRepository(
    private val context: Context
) {

    fun loadApps(): List<LauncherApp> {
        val packageManager = context.packageManager

        val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val activities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                launcherIntent,
                PackageManager.ResolveInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(launcherIntent, 0)
        }

        val collator = Collator.getInstance(Locale("pt", "BR")).apply {
            strength = Collator.PRIMARY
        }

        return activities
            .mapNotNull { resolveInfo ->
                val activityInfo = resolveInfo.activityInfo ?: return@mapNotNull null

                // Evita que o próprio launcher apareça na gaveta.
                if (activityInfo.packageName == context.packageName) {
                    return@mapNotNull null
                }

                runCatching {
                    val drawable = resolveInfo.loadIcon(packageManager)

                    // Tamanho limitado para não manter bitmaps enormes na memória.
                    val bitmap = drawable.toBitmap(
                        width = 100,
                        height = 100
                    )


                    LauncherApp(
                        label = resolveInfo
                            .loadLabel(packageManager)
                            .toString()
                            .trim(),
                        packageName = activityInfo.packageName,
                        activityName = activityInfo.name
                    )
                }.getOrNull()
            }
            .distinctBy { it.componentName() }
            .sortedWith { first, second ->
                collator.compare(first.label, second.label)
            }
    }

    companion object {
        // Cor usada quando o ícone não possui uma cor dominante detectável.
        private const val DEFAULT_TILE_COLOR = 0xFF6750A4.toInt()
    }
}