package org.prewave.task.entity.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class EdgeDTO (
    @NotNull
    var fromId: Int? = null,

    @NotNull
    var toId: Int? = null
)
