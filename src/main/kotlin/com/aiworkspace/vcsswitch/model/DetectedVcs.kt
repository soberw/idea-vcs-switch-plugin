package com.aiworkspace.vcsswitch.model

import java.nio.file.Files
import java.nio.file.Path

enum class DetectedVcs(
    val mappingName: String,
    val displayName: String,
    private val markerName: String,
) {
    GIT(mappingName = "Git", displayName = "Git", markerName = ".git"),
    SVN(mappingName = "svn", displayName = "SVN", markerName = ".svn");

    fun isDetectedAt(projectRoot: Path): Boolean = Files.exists(projectRoot.resolve(markerName))

    companion object {
        fun fromMappingName(mappingName: String?): DetectedVcs? {
            return entries.firstOrNull { vcs ->
                vcs.mappingName.equals(mappingName, ignoreCase = true)
            }
        }
    }
}
