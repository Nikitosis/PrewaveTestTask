package org.prewave.task.entity.dto

data class TreeDTO (
    var nodeId: Int? = null,
    var children: MutableList<TreeDTO>? = null
)
