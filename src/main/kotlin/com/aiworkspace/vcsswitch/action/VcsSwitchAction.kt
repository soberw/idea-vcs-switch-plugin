package com.aiworkspace.vcsswitch.action

import com.aiworkspace.vcsswitch.model.DetectedVcs
import com.aiworkspace.vcsswitch.model.ProjectVcsState
import com.aiworkspace.vcsswitch.notification.VcsSwitchNotifications
import com.aiworkspace.vcsswitch.service.VcsSwitchProjectService
import com.aiworkspace.vcsswitch.switcher.VcsMappingSwitcher
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory

class VcsSwitchAction : DumbAwareAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isEnabled = false
            e.presentation.text = "Switch Project VCS"
            return
        }

        val state = VcsSwitchProjectService.getInstance(project).refreshState()
        e.presentation.isEnabled = state.hasDetectedVcs
        e.presentation.text = buildPresentationText(state.currentVcs)
        e.presentation.description = buildDescription(state)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = VcsSwitchProjectService.getInstance(project)
        val state = service.refreshState()
        if (!state.hasDetectedVcs) {
            VcsSwitchNotifications.warning(project, "未识别到可切换的 VCS", "当前项目根目录下没有检测到 Git 或 SVN。")
            return
        }

        val popupGroup = DefaultActionGroup().apply {
            state.availableVcs.forEach { vcs ->
                add(createSwitchAction(project, service, state, vcs))
            }
        }

        JBPopupFactory.getInstance()
            .createActionGroupPopup(
                "Switch Project VCS",
                popupGroup,
                e.dataContext,
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true,
            )
            .showInBestPositionFor(e.dataContext)
    }

    private fun createSwitchAction(
        project: Project,
        service: VcsSwitchProjectService,
        state: ProjectVcsState,
        targetVcs: DetectedVcs,
    ): DumbAwareAction {
        val isCurrent = targetVcs == state.currentVcs
        val itemText = if (isCurrent) "${targetVcs.displayName} (current)" else targetVcs.displayName

        return object : DumbAwareAction(itemText, null, if (isCurrent) AllIcons.Actions.Checked else null) {
            override fun actionPerformed(e: AnActionEvent) {
                if (isCurrent) {
                    VcsSwitchNotifications.info(project, "无需切换", "当前项目已经在使用 ${targetVcs.displayName}。")
                    return
                }

                VcsSwitchNotifications.info(project, "正在切换 VCS", "正在切换到 ${targetVcs.displayName}，请稍候。")
                VcsMappingSwitcher(project).switchTo(
                    targetVcs = targetVcs,
                    onSuccess = {
                        service.refreshState()
                        VcsSwitchNotifications.info(project, "VCS 切换完成", "当前项目已切换到 ${targetVcs.displayName}。")
                    },
                    onError = { message ->
                        VcsSwitchNotifications.error(project, "VCS 切换失败", message)
                    },
                )
            }
        }
    }

    private fun buildPresentationText(currentVcs: DetectedVcs?): String {
        return currentVcs?.let { "Switch Project VCS (${it.displayName})" } ?: "Switch Project VCS"
    }

    private fun buildDescription(state: ProjectVcsState): String {
        if (!state.hasDetectedVcs) {
            return "No supported VCS was detected in the project root."
        }

        val detected = state.availableVcs.joinToString(separator = ", ") { it.displayName }
        val current = state.currentVcs?.displayName ?: "None"
        return "Detected: $detected. Current: $current."
    }
}
