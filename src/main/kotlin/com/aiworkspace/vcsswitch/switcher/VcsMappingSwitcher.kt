package com.aiworkspace.vcsswitch.switcher

import com.aiworkspace.vcsswitch.model.DetectedVcs
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vcs.VcsDirectoryMapping
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vcs.changes.InvokeAfterUpdateMode
import java.nio.file.InvalidPathException
import java.nio.file.Path

class VcsMappingSwitcher(private val project: Project) {
    private val vcsManager = ProjectLevelVcsManager.getInstance(project)
    private val changeListManager = ChangeListManager.getInstance(project)

    fun switchTo(
        targetVcs: DetectedVcs,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val projectRoot = project.basePath ?: run {
            onError("无法确定当前项目根目录。")
            return
        }

        val normalizedRoot = normalizePath(projectRoot)

        try {
            val updatedMappings = vcsManager.directoryMappings
                .filterNot { mapping ->
                    mapping.isDefaultMapping || normalizePath(mapping.directory) == normalizedRoot
                }
                .toMutableList()
                .apply {
                    add(VcsDirectoryMapping(projectRoot, targetVcs.mappingName))
                }

            vcsManager.setDirectoryMappings(updatedMappings)
            changeListManager.invokeAfterUpdate(
                Runnable { onSuccess() },
                InvokeAfterUpdateMode.SILENT,
                "Switching project VCS",
                ModalityState.nonModal(),
            )
        } catch (error: Exception) {
            onError(error.message ?: "切换 VCS 时发生未知错误。")
        }
    }

    private fun normalizePath(path: String?): String? {
        if (path.isNullOrBlank()) {
            return null
        }

        return try {
            Path.of(path).toAbsolutePath().normalize().toString().replace('\\', '/')
        } catch (_: InvalidPathException) {
            null
        }
    }
}
