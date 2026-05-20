package com.aiworkspace.vcsswitch.startup

import com.aiworkspace.vcsswitch.service.VcsSwitchProjectService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class VcsSwitchStartupActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        VcsSwitchProjectService.getInstance(project).refreshState()
    }
}
