package com.makoto.projectopener

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.util.ui.JBUI
import java.awt.Component
import java.nio.file.Paths
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

class GoToProjectAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val discoveryService = ProjectDiscoveryService.getInstance()
        val settings = ProjectOpenerSettings.getInstance()
        val dataContext = e.dataContext

        ApplicationManager.getApplication().executeOnPooledThread {
            val projects = discoveryService.findProjects("")
            invokeLater {
                showProjectPopup(projects, settings.forceOpenInNewWindow, dataContext, e.project)
            }
        }
    }

    private fun showProjectPopup(
        allProjects: List<ProjectInfo>,
        forceNewWindow: Boolean,
        dataContext: com.intellij.openapi.actionSystem.DataContext,
        project: com.intellij.openapi.project.Project?
    ) {
        if (allProjects.isEmpty()) {
            com.intellij.openapi.ui.Messages.showMessageDialog(
                project,
                "No projects found. Add project root paths in Settings | Tools | Project Opener Makoto.",
                "No Projects",
                com.intellij.openapi.ui.Messages.getInformationIcon()
            )
            return
        }

        val popup = JBPopupFactory.getInstance()
            .createPopupChooserBuilder(allProjects)
            .setTitle("Enter Project Name")
            .setRenderer(object : DefaultListCellRenderer() {
                override fun getListCellRendererComponent(
                    list: JList<*>, value: Any?, index: Int,
                    isSelected: Boolean, cellHasFocus: Boolean
                ): Component {
                    val c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                    text = (value as? ProjectInfo)?.displayText() ?: ""
                    border = JBUI.Borders.empty(2, 8)
                    return c
                }
            })
            .setNamerForFiltering { it.name }
            .setItemChosenCallback { openProject(it.path, forceNewWindow) }
            .setRequestFocus(true)
            .setFilterAlwaysVisible(true)
            .createPopup()

        if (project != null) {
            popup.showCenteredInCurrentWindow(project)
        } else {
            popup.showInBestPositionFor(dataContext)
        }
    }

    private fun openProject(path: String, forceNewWindow: Boolean) {
        val file = Paths.get(path).normalize()
        ProjectUtil.openOrImport(file, null, forceNewWindow)
    }
}
