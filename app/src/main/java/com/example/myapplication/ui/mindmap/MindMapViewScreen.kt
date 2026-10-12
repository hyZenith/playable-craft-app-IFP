package com.example.myapplication.ui.mindmap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CloseFullscreen
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.MindMapDetail
import com.example.myapplication.data.model.MindMapNode
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import kotlin.math.roundToInt

private val Lavender = Color(0xFF8F9BD6)
private val BoardBg = Color(0xFF040814)
private val BoardBorder = Color(0xFF172445)
private val ControlBtnBg = Color(0xFF0F1832)
private val WireColor = Color(0xFF2C4376)

/**
 * Stateful entry point route for viewing and interacting with a concept Mind Map tree.
 *
 * @param viewModel Controller managing tree expansion and navigation.
 * @param onBackClick Callback triggered when returning to the previous view.
 */
@Composable
fun MindMapViewRoute(
    viewModel: MindMapViewViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.uiState

    MindMapViewScreen(
        state = state,
        onBackClick = onBackClick,
        onToggleNode = viewModel::toggleNode,
        onExpandAll = viewModel::expandAll,
        onCollapseAll = viewModel::collapseAll,
        onToggleOrientation = viewModel::toggleOrientation,
        onZoomChange = viewModel::onZoomChange,
        onRetry = viewModel::retry
    )
}

/**
 * Stateless screen rendering the Mind Map header, toolbar actions,
 * and the interactive infinite canvas tree viewer.
 */
@Composable
fun MindMapViewScreen(
    state: MindMapViewUiState,
    onBackClick: () -> Unit,
    onToggleNode: (String) -> Unit,
    onExpandAll: () -> Unit,
    onCollapseAll: () -> Unit,
    onToggleOrientation: () -> Unit,
    onZoomChange: (Float) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        MindMapTopHeader(
            detail = state.mapDetail,
            onBackClick = onBackClick
        )

        Spacer(Modifier.height(10.dp))

        MindMapToolbarRow(
            onExpandAll = onExpandAll,
            onCollapseAll = onCollapseAll,
            onToggleOrientation = onToggleOrientation,
            isHorizontal = state.isHorizontal
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(BoardBg)
                .border(1.dp, BoardBorder, RoundedCornerShape(16.dp))
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = AccentLink,
                        strokeWidth = 3.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp)
                    )
                }

                state.errorMessage != null -> {
                    ErrorState(
                        message = state.errorMessage,
                        onRetry = onRetry
                    )
                }

                state.mapDetail != null -> {
                    InteractiveMindMapCanvas(
                        root = state.mapDetail.root,
                        collapsedIds = state.collapsedNodeIds,
                        zoomScale = state.zoomScale,
                        onToggleNode = onToggleNode
                    )
                }
            }
        }
    }
}

/**
 * Top header displaying back navigation, title, and uppercase breadcrumb info.
 */
@Composable
private fun MindMapTopHeader(
    detail: MindMapDetail?,
    onBackClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ControlBtnBg)
                .border(1.dp, FieldStroke, RoundedCornerShape(10.dp))
                .clickable(onClick = onBackClick)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text = detail?.title ?: "Mind Map",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = detail?.breadcrumb ?: "",
                color = Lavender,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Toolbar housing expansion actions and horizontal/vertical layout toggle.
 */
@Composable
private fun MindMapToolbarRow(
    onExpandAll: () -> Unit,
    onCollapseAll: () -> Unit,
    onToggleOrientation: () -> Unit,
    isHorizontal: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToolbarButton(
                label = "Expand all",
                icon = Icons.Outlined.OpenInFull,
                onClick = onExpandAll
            )

            ToolbarButton(
                label = "Collapse all",
                icon = Icons.Outlined.CloseFullscreen,
                onClick = onCollapseAll
            )
        }

        ToolbarButton(
            label = if (isHorizontal) "Horizontal" else "Vertical",
            icon = Icons.Outlined.CompareArrows,
            onClick = onToggleOrientation
        )
    }
}

/**
 * Small pill button used in the Mind Map viewer toolbar.
 */
@Composable
private fun ToolbarButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(32.dp)
            .clip(shape)
            .background(ControlBtnBg)
            .border(1.dp, FieldStroke, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFCAD5F5),
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            color = Color(0xFFCAD5F5),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Data representation of a positioned node on the 2D canvas.
 */
private data class PositionedNode(
    val node: MindMapNode,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val isCollapsed: Boolean,
    val hasChildren: Boolean,
    val children: List<PositionedNode>
)

/**
 * Infinite 2D viewport rendering nodes and dynamic connecting cubic bezier curves.
 */
@Composable
private fun InteractiveMindMapCanvas(
    root: MindMapNode,
    collapsedIds: Set<String>,
    zoomScale: Float,
    onToggleNode: (String) -> Unit
) {
    val density = LocalDensity.current
    var panOffsetX by remember { mutableFloatStateOf(40f) }
    var panOffsetY by remember { mutableFloatStateOf(40f) }

    val horizontalScroll = rememberScrollState()
    val verticalScroll = rememberScrollState()

    val nodeWidthPx = with(density) { 190.dp.toPx() }
    val levelGapPx = with(density) { 65.dp.toPx() }
    val leafGapPx = with(density) { 16.dp.toPx() }

    val layoutResult = remember(root, collapsedIds) {
        computeTreeLayout(
            node = root,
            collapsedIds = collapsedIds,
            level = 0,
            startX = 40f,
            nodeWidth = nodeWidthPx,
            levelGap = levelGapPx,
            leafGap = leafGapPx,
            density = density
        )
    }

    val totalWidthDp = with(density) { (layoutResult.maxX + 120f).toDp() }
    val totalHeightDp = with(density) { (layoutResult.maxY + 120f).toDp() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    panOffsetX += pan.x
                    panOffsetY += pan.y
                }
            }
            .horizontalScroll(horizontalScroll)
            .verticalScroll(verticalScroll)
    ) {
        Box(
            modifier = Modifier
                .size(width = totalWidthDp, height = totalHeightDp)
                .graphicsLayer {
                    scaleX = zoomScale
                    scaleY = zoomScale
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawConnectingBezierCurves(layoutResult.root)
            }

            RenderNodeViews(
                positionedNode = layoutResult.root,
                density = density,
                onToggleNode = onToggleNode
            )
        }
    }
}

/**
 * Recursively computes positions of visible nodes, calculating leaf offsets
 * and centering parents between their children.
 */
private data class LayoutResult(
    val root: PositionedNode,
    val maxX: Float,
    val maxY: Float
)

private fun computeTreeLayout(
    node: MindMapNode,
    collapsedIds: Set<String>,
    level: Int,
    startX: Float,
    nodeWidth: Float,
    levelGap: Float,
    leafGap: Float,
    density: androidx.compose.ui.unit.Density
): LayoutResult {
    var globalY = 40f
    var maxX = startX + nodeWidth

    fun layoutNode(current: MindMapNode, currentLevel: Int, currentX: Float): PositionedNode {
        val lineCount = current.label.count { it == '\n' } + 1
        val heightPx = with(density) { (38 + lineCount * 14).dp.toPx() }
        val isCollapsed = collapsedIds.contains(current.id)
        val hasChildren = current.children.isNotEmpty()

        if (currentX + nodeWidth > maxX) {
            maxX = currentX + nodeWidth
        }

        if (hasChildren && !isCollapsed) {
            val childNodes = mutableListOf<PositionedNode>()
            val nextX = currentX + nodeWidth + levelGap

            current.children.forEach { child ->
                val laidChild = layoutNode(child, currentLevel + 1, nextX)
                childNodes.add(laidChild)
            }

            val firstChildY = childNodes.first().y + (childNodes.first().height / 2f)
            val lastChildY = childNodes.last().y + (childNodes.last().height / 2f)
            val parentCenterY = (firstChildY + lastChildY) / 2f
            val parentY = parentCenterY - (heightPx / 2f)

            return PositionedNode(
                node = current,
                x = currentX,
                y = parentY,
                width = nodeWidth,
                height = heightPx,
                isCollapsed = false,
                hasChildren = true,
                children = childNodes
            )
        } else {
            val assignedY = globalY
            globalY += heightPx + leafGap

            return PositionedNode(
                node = current,
                x = currentX,
                y = assignedY,
                width = nodeWidth,
                height = heightPx,
                isCollapsed = isCollapsed,
                hasChildren = hasChildren,
                children = emptyList()
            )
        }
    }

    val laidRoot = layoutNode(node, level, startX)
    return LayoutResult(root = laidRoot, maxX = maxX, maxY = globalY)
}

/**
 * Draws smooth cubic bezier curves connecting the right edge of a parent
 * to the left edge of each child node.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawConnectingBezierCurves(
    node: PositionedNode
) {
    if (!node.isCollapsed && node.children.isNotEmpty()) {
        val startX = node.x + node.width
        val startY = node.y + (node.height / 2f)

        node.children.forEach { child ->
            val endX = child.x
            val endY = child.y + (child.height / 2f)

            val deltaX = endX - startX
            val controlPointX1 = startX + (deltaX * 0.45f)
            val controlPointX2 = startX + (deltaX * 0.55f)

            val path = Path().apply {
                moveTo(startX, startY)
                cubicTo(
                    x1 = controlPointX1,
                    y1 = startY,
                    x2 = controlPointX2,
                    y2 = endY,
                    x3 = endX,
                    y3 = endY
                )
            }

            drawPath(
                path = path,
                color = WireColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            drawConnectingBezierCurves(child)
        }
    }
}

/**
 * Recursively renders node cards and expand/collapse toggle chevrons.
 */
@Composable
private fun RenderNodeViews(
    positionedNode: PositionedNode,
    density: androidx.compose.ui.unit.Density,
    onToggleNode: (String) -> Unit
) {
    val xDp = with(density) { positionedNode.x.toDp() }
    val yDp = with(density) { positionedNode.y.toDp() }
    val widthDp = with(density) { positionedNode.width.toDp() }
    val heightDp = with(density) { positionedNode.height.toDp() }

    MindMapNodeCard(
        node = positionedNode.node,
        x = xDp,
        y = yDp,
        width = widthDp,
        height = heightDp,
        isCollapsed = positionedNode.isCollapsed,
        hasChildren = positionedNode.hasChildren,
        onToggle = { onToggleNode(positionedNode.node.id) }
    )

    if (!positionedNode.isCollapsed) {
        positionedNode.children.forEach { child ->
            RenderNodeViews(
                positionedNode = child,
                density = density,
                onToggleNode = onToggleNode
            )
        }
    }
}

/**
 * Visual styling for each hierarchy level matching the reference design.
 */
@Composable
private fun MindMapNodeCard(
    node: MindMapNode,
    x: Dp,
    y: Dp,
    width: Dp,
    height: Dp,
    isCollapsed: Boolean,
    hasChildren: Boolean,
    onToggle: () -> Unit
) {
    val level = node.level

    val bg = when (level) {
        0 -> Color(0xFF0F1832)
        1 -> Color(0xFF0A2046)
        2 -> Color(0xFF1D143D)
        else -> Color(0xFF26190B)
    }

    val stroke = when (level) {
        0 -> Color(0xFF8F9BD6)
        1 -> Color(0xFF2460C4)
        2 -> Color(0xFF673AB7)
        else -> Color(0xFFB57418)
    }

    val textColor = when (level) {
        0 -> Color.White
        1 -> Color.White
        2 -> Color.White
        else -> Color(0xFFFFD599)
    }

    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = Modifier
            .offset { IntOffset(x.toPx().roundToInt(), y.toPx().roundToInt()) }
            .width(width)
            .height(height)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(bg)
                .border(1.dp, stroke, shape)
                .clickable { if (hasChildren) onToggle() }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = node.label,
                color = textColor,
                fontSize = if (level <= 1) 13.5.sp else 12.sp,
                fontWeight = if (level <= 1) FontWeight.Bold else FontWeight.Medium,
                lineHeight = 16.sp
            )
        }

        if (hasChildren) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 10.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0D1630))
                    .border(1.dp, Color(0xFF384D7A), CircleShape)
                    .clickable(onClick = onToggle)
            ) {
                Icon(
                    imageVector = if (isCollapsed) Icons.Filled.KeyboardArrowRight else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = Color(0xFF8F9BD6),
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

/**
 * Error state with retry callback for failed queries.
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unable to load Mind Map canvas",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = message,
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FieldStroke),
            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
            Text("Try again")
        }
    }
}
