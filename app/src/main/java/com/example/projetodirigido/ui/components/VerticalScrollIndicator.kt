package com.example.projetodirigido.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

/**
 * Barra de rolagem real (não decorativa): calcula altura e posição da "pílula"
 * a partir do estado de scroll do LazyVerticalGrid, então some quando o
 * conteúdo cabe todo na tela e acompanha o dedo do usuário durante o scroll.
 *
 * Uso: dentro de um Box que envolve o LazyVerticalGrid, alinhado à direita.
 *
 *  Box(Modifier.fillMaxSize()) {
 *      LazyVerticalGrid(state = gridState, ...)
 *      VerticalScrollIndicator(
 *          state = gridState,
 *          modifier = Modifier.align(Alignment.CenterEnd)
 *      )
 *  }
 */
@Composable
fun VerticalScrollIndicator(
    state: LazyGridState,
    trackColor: Color,
    thumbColor: Color,
    modifier: Modifier = Modifier
) {
    val showScrollbar by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            layoutInfo.totalItemsCount > 0 &&
                    layoutInfo.visibleItemsInfo.size < layoutInfo.totalItemsCount
        }
    }

    if (!showScrollbar) return

    // Progresso (0f..1f) calculado a partir do índice/offset do primeiro item
    // visível, considerando quantas "linhas" existem no total.
    val scrollFraction by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val columns = layoutInfo.visibleItemsInfo
                .maxOfOrNull { it.column + 1 }
                ?.coerceAtLeast(1) ?: 1
            val totalRows = kotlin.math.ceil(layoutInfo.totalItemsCount / columns.toFloat())
            val firstRow = state.firstVisibleItemIndex / columns
            val rowSizePx = layoutInfo.visibleItemsInfo.firstOrNull()?.size?.height ?: 1
            val extraRowFraction = if (rowSizePx > 0) {
                state.firstVisibleItemScrollOffset / rowSizePx.toFloat()
            } else 0f
            val visibleRows = (layoutInfo.viewportSize.height / rowSizePx.coerceAtLeast(1).toFloat())
                .coerceAtLeast(1f)

            val maxScrollableRows = (totalRows - visibleRows).coerceAtLeast(0.01f)
            (((firstRow + extraRowFraction) / maxScrollableRows)).coerceIn(0f, 1f)
        }
    }

    val thumbHeightFraction by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val columns = layoutInfo.visibleItemsInfo
                .maxOfOrNull { it.column + 1 }
                ?.coerceAtLeast(1) ?: 1
            val totalRows = kotlin.math.ceil(layoutInfo.totalItemsCount / columns.toFloat())
                .coerceAtLeast(1f)
            val rowSizePx = layoutInfo.visibleItemsInfo.firstOrNull()?.size?.height ?: 1
            val visibleRows = (layoutInfo.viewportSize.height / rowSizePx.coerceAtLeast(1).toFloat())
                .coerceAtLeast(1f)
            (visibleRows / totalRows).coerceIn(0.08f, 1f)
        }
    }

    Canvas(
        modifier = modifier
            .width(6.dp)
            .fillMaxHeight()
    ) {
        drawTrackAndThumb(
            trackColor = trackColor,
            thumbColor = thumbColor,
            thumbHeightFraction = thumbHeightFraction,
            scrollFraction = scrollFraction
        )
    }
}

private fun DrawScope.drawTrackAndThumb(
    trackColor: Color,
    thumbColor: Color,
    thumbHeightFraction: Float,
    scrollFraction: Float
) {
    val trackWidth = size.width
    val cornerRadius = trackWidth / 2f

    // Trilho de fundo (sempre visível, sutil).
    drawRoundRect(
        color = trackColor,
        size = Size(trackWidth, size.height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
    )

    // "Pílula" que se move conforme o usuário rola a lista.
    val thumbHeight = size.height * thumbHeightFraction
    val maxThumbTravel = size.height - thumbHeight
    val thumbTop = maxThumbTravel * scrollFraction

    drawRoundRect(
        color = thumbColor,
        topLeft = Offset(0f, thumbTop),
        size = Size(trackWidth, thumbHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
    )
}