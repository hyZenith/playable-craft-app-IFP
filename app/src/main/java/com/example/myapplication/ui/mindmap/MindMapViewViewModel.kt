package com.example.myapplication.ui.mindmap

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.MindMapDetail
import com.example.myapplication.data.model.MindMapNode
import com.example.myapplication.data.repo.MindMapRepository
import kotlinx.coroutines.launch

/**
 * Immutable UI state for the interactive Mind Map tree viewer.
 *
 * @property mapDetail Complete hierarchical concept graph.
 * @property collapsedNodeIds Set of node identifiers whose children are currently collapsed.
 * @property isHorizontal Whether nodes are arranged horizontally (left-to-right) or vertically.
 * @property zoomScale Current canvas zoom magnification factor.
 * @property isLoading Indicates whether graph data is being loaded.
 * @property errorMessage Non-null if an error occurred while retrieving graph details.
 */
data class MindMapViewUiState(
    val mapDetail: MindMapDetail? = null,
    val collapsedNodeIds: Set<String> = emptySet(),
    val isHorizontal: Boolean = true,
    val zoomScale: Float = 1.0f,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * ViewModel governing expansion states, graph traversal, and camera controls for the Mind Map viewer.
 */
class MindMapViewViewModel(
    private val repository: MindMapRepository,
    private val mapId: String
) : ViewModel() {

    var uiState by mutableStateOf(MindMapViewUiState())
        private set

    init {
        loadDetail()
    }

    /**
     * Toggles the collapsed/expanded state of a specific node's branch.
     */
    fun toggleNode(nodeId: String) {
        val currentCollapsed = uiState.collapsedNodeIds
        val updated = if (currentCollapsed.contains(nodeId)) {
            currentCollapsed - nodeId
        } else {
            currentCollapsed + nodeId
        }
        uiState = uiState.copy(collapsedNodeIds = updated)
    }

    /**
     * Expands all branches across the entire concept hierarchy.
     */
    fun expandAll() {
        uiState = uiState.copy(collapsedNodeIds = emptySet())
    }

    /**
     * Collapses all intermediate nodes down to high-level topics.
     */
    fun collapseAll() {
        val detail = uiState.mapDetail ?: return
        val allParentIds = mutableSetOf<String>()
        collectParentIds(detail.root, allParentIds)
        // Keep root and level 1 visible, collapse level 2 and beyond
        allParentIds.remove(detail.root.id)
        uiState = uiState.copy(collapsedNodeIds = allParentIds)
    }

    /**
     * Toggles between horizontal and vertical orientation layouts.
     */
    fun toggleOrientation() {
        uiState = uiState.copy(isHorizontal = !uiState.isHorizontal)
    }

    /**
     * Updates canvas zoom level within safe bounds.
     */
    fun onZoomChange(delta: Float) {
        val newScale = (uiState.zoomScale * delta).coerceIn(0.5f, 2.5f)
        uiState = uiState.copy(zoomScale = newScale)
    }

    /**
     * Retries loading data.
     */
    fun retry() {
        loadDetail()
    }

    private fun collectParentIds(node: MindMapNode, result: MutableSet<String>) {
        if (node.children.isNotEmpty()) {
            result.add(node.id)
            node.children.forEach { collectParentIds(it, result) }
        }
    }

    private fun loadDetail() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            repository.getMindMapDetail(mapId)
                .onSuccess { detail ->
                    uiState = uiState.copy(
                        isLoading = false,
                        mapDetail = detail
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Unable to load Mind Map canvas. Please try again."
                    )
                }
        }
    }
}
