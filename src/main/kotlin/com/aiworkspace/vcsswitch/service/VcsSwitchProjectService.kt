package com.aiworkspace.vcsswitch.service

import com.aiworkspace.vcsswitch.detector.ProjectVcsDetector
import com.aiworkspace.vcsswitch.model.ProjectVcsState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class VcsSwitchProjectService(private val project: Project) {
    private val detector = ProjectVcsDetector(project)

    @Volatile
    private var state: ProjectVcsState = ProjectVcsState.empty()

    fun refreshState(): ProjectVcsState {
        val latestState = detector.detect()
        state = latestState
        return latestState
    }

    fun currentState(): ProjectVcsState = state

    companion object {
        fun getInstance(project: Project): VcsSwitchProjectService = project.service()
    }
}
