package com.aiworkspace.vcsswitch.model

import java.nio.file.Path

data class ProjectVcsState(
    val projectRoot: Path?,
    val availableVcs: List<DetectedVcs>,
    val currentVcs: DetectedVcs?,
) {
    val hasDetectedVcs: Boolean
        get() = availableVcs.isNotEmpty()

    companion object {
        fun empty(): ProjectVcsState = ProjectVcsState(
            projectRoot = null,
            availableVcs = emptyList(),
            currentVcs = null,
        )
    }
}
