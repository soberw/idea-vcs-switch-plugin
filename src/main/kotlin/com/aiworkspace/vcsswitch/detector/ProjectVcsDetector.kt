package com.aiworkspace.vcsswitch.detector

import com.aiworkspace.vcsswitch.model.DetectedVcs
import com.aiworkspace.vcsswitch.model.ProjectVcsState
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import java.nio.file.InvalidPathException
import java.nio.file.Path

class ProjectVcsDetector(private val project: Project) {
    private val vcsManager = ProjectLevelVcsManager.getInstance(project)

    fun detect(): ProjectVcsState {
        val rootPath = project.basePath?.let(::safePathOf) ?: return ProjectVcsState.empty()
        val availableVcs = DetectedVcs.entries.filter { vcs ->
            vcs.isDetectedAt(rootPath) && vcsManager.findVcsByName(vcs.mappingName) != null
        }

        return ProjectVcsState(
            projectRoot = rootPath,
            availableVcs = availableVcs,
            currentVcs = resolveCurrentVcs(rootPath),
        )
    }

    private fun resolveCurrentVcs(rootPath: Path): DetectedVcs? {
        val normalizedRoot = normalizePath(rootPath)
        val mappingName = vcsManager.directoryMappings
            .firstOrNull { mapping ->
                val directory = mapping.directory
                if (directory.isNullOrBlank()) {
                    return@firstOrNull false
                }

                val mappedDirectory = safePathOf(directory) ?: return@firstOrNull false
                normalizePath(mappedDirectory) == normalizedRoot
            }
            ?.vcs
            ?: vcsManager.directoryMappings.firstOrNull { it.isDefaultMapping }?.vcs
            ?: project.guessProjectDir()?.let(vcsManager::getVcsFor)?.name

        return DetectedVcs.fromMappingName(mappingName)
    }

    private fun safePathOf(path: String): Path? = try {
        Path.of(path)
    } catch (_: InvalidPathException) {
        null
    }

    private fun normalizePath(path: Path): String {
        return path.toAbsolutePath().normalize().toString().replace('\\', '/')
    }
}
