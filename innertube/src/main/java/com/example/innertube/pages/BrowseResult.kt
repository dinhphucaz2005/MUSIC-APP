package com.example.innertube.pages

import com.example.innertube.models.YTItem
import com.example.innertube.models.filterExplicit

data class BrowseResult(
    val title: String?,
    val items: List<Item>,
) {
    data class Item(
        val title: String?,
        val items: List<YTItem>,
    )

    fun filterExplicit(enabled: Boolean = true) =
        if (enabled) {
            copy(
                items = items.mapNotNull {
                    it.copy(
                        items = it.items
                            .filterExplicit()
                            .ifEmpty { return@mapNotNull null }
                    )
                }
            )
        } else this
}