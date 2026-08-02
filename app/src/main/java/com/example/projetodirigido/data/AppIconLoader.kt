package com.example.projetodirigido.data

import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.LruCache
import androidx.core.graphics.drawable.toBitmap
import com.example.projetodirigido.model.LauncherApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.ConcurrentHashMap

data class AppIconData(
    val bitmap: Bitmap,
    val tileColor: Int
)

class AppIconLoader(
    context: Context
) {
    private val packageManager =
        context.applicationContext.packageManager

    /*
     * Máximo de 8 MiB de bitmaps.
     *
     * Quando o limite é alcançado, os ícones usados há mais tempo
     * são removidos automaticamente.
     */
    private val memoryCache =
        object : LruCache<String, AppIconData>(12 * 1024 * 1024) {
            override fun sizeOf(
                key: String,
                value: AppIconData
            ): Int {
                return value.bitmap.allocationByteCount
            }
        }

    /*
     * Evita que dois blocos solicitem simultaneamente o mesmo ícone.
     */
    private val requests =
        ConcurrentHashMap<String, Deferred<AppIconData?>>()

    /*
     * Apenas dois ícones são decodificados ao mesmo tempo.
     * Isso evita um pico de CPU ao fazer scroll rápido.
     */
    private val decoderLimit = Semaphore(permits = 4)

    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun load(
        app: LauncherApp,
        targetSizePx: Int
    ): AppIconData? {
        val cacheKey = "${app.key}@$targetSizePx"

        memoryCache.get(cacheKey)?.let {
            return it
        }

        val newRequest = scope.async(
            start = CoroutineStart.LAZY
        ) {
            decoderLimit.withPermit {
                decodeAppIcon(
                    app = app,
                    targetSizePx = targetSizePx
                )
            }
        }

        val activeRequest =
            requests.putIfAbsent(cacheKey, newRequest)
                ?: newRequest.also { it.start() }

        if (activeRequest !== newRequest) {
            newRequest.cancel()
        }

        return try {
            activeRequest.await()?.also { result ->
                memoryCache.put(cacheKey, result)
            }
        } finally {
            if (activeRequest.isCompleted) {
                requests.remove(cacheKey, activeRequest)
            }
        }
    }

    private fun decodeAppIcon(
        app: LauncherApp,
        targetSizePx: Int
    ): AppIconData? {
        return runCatching {
            val componentName = ComponentName(
                app.packageName,
                app.activityName
            )

            val drawable =
                packageManager.getActivityIcon(componentName)

            val bitmap = drawable.toBitmap(
                width = targetSizePx,
                height = targetSizePx,
                config = Bitmap.Config.ARGB_8888
            )

            AppIconData(
                bitmap = bitmap,
                tileColor = findCenterColor(
                    bitmap = bitmap,
                    fallback = fallbackColorFor(
                        app.packageName
                    )
                )
            )
        }.getOrNull()
    }

    /**
     * Calcula uma média simples da região central do ícone.
     * É muito mais barato do que executar Palette.
     */
    private fun findCenterColor(
        bitmap: Bitmap,
        fallback: Int
    ): Int {
        val startX = bitmap.width * 35 / 100
        val endX = bitmap.width * 65 / 100
        val startY = bitmap.height * 35 / 100
        val endY = bitmap.height * 65 / 100

        var red = 0L
        var green = 0L
        var blue = 0L
        var count = 0L

        for (y in startY until endY step 2) {
            for (x in startX until endX step 2) {
                val pixel = bitmap.getPixel(x, y)

                if (Color.alpha(pixel) >= 128) {
                    red += Color.red(pixel)
                    green += Color.green(pixel)
                    blue += Color.blue(pixel)
                    count++
                }
            }
        }

        if (count == 0L) {
            return fallback
        }

        val result = Color.rgb(
            (red / count).toInt(),
            (green / count).toInt(),
            (blue / count).toInt()
        )

        val hsv = FloatArray(3)
        Color.colorToHSV(result, hsv)

        // Evita blocos quase brancos, cinza ou quase pretos.
        return if (
            hsv[1] < 0.12f ||
            hsv[2] > 0.94f ||
            hsv[2] < 0.14f
        ) {
            fallback
        } else {
            result
        }
    }

    fun close() {
        requests.values.forEach { it.cancel() }
        requests.clear()
        memoryCache.evictAll()
        scope.cancel()
    }

    companion object {
        private val fallbackColors = intArrayOf(
            0xFF0061A4.toInt(),
            0xFF006C4C.toInt(),
            0xFF6750A4.toInt(),
            0xFF7D5260.toInt(),
            0xFF8C5000.toInt(),
            0xFF006874.toInt()
        )

        fun fallbackColorFor(packageName: String): Int {
            val positiveHash =
                packageName.hashCode().toLong() and 0x7FFFFFFF

            return fallbackColors[
                (positiveHash % fallbackColors.size).toInt()
            ]
        }
    }
}