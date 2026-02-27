package com.makoto.projectopener

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.ListSpeedSearch
import javax.swing.JComponent
import javax.swing.JPanel

class ProjectOpenerConfigurable : Configurable {

    private var pathsListModel: CollectionListModel<String>? = null
    private var mainPanel: JPanel? = null
    private var forceNewWindowCheckbox: com.intellij.ui.components.JBCheckBox? = null

    @NlsContexts.ConfigurableName
    override fun getDisplayName(): String = "Project Opener Makoto"

    override fun createComponent(): JComponent {
        val settings = ProjectOpenerSettings.getInstance()
        pathsListModel = CollectionListModel(settings.projectsRootPaths.toMutableList())
        forceNewWindowCheckbox = com.intellij.ui.components.JBCheckBox("Force to open project in new window", settings.forceOpenInNewWindow)

        val list = JBList<String>().apply {
            model = pathsListModel!!
        }
        ListSpeedSearch.installOn(list) { it ?: "" }

        val listPanel = ToolbarDecorator.createDecorator(list)
            .setAddAction {
                val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
                FileChooser.chooseFiles(descriptor, null, null) { virtualFiles ->
                    virtualFiles.forEach { vf ->
                        val path = vf.path
                        if (path !in pathsListModel!!) {
                            pathsListModel!!.add(path)
                        }
                    }
                }
            }
            .setRemoveAction {
                list.selectedIndices.reversed().forEach { pathsListModel!!.remove(it) }
            }
            .createPanel()

        mainPanel = panel {
            row("Projects root path:") {
                cell(listPanel).resizableColumn()
            }
            row {
                cell(forceNewWindowCheckbox!!)
            }
        }

        return mainPanel!!
    }

    override fun isModified(): Boolean {
        val settings = ProjectOpenerSettings.getInstance()
        val currentPaths = pathsListModel?.items?.toList() ?: emptyList()
        val savedPaths = settings.projectsRootPaths
        if (currentPaths != savedPaths) return true
        if (forceNewWindowCheckbox?.isSelected != settings.forceOpenInNewWindow) return true
        return false
    }

    override fun apply() {
        val settings = ProjectOpenerSettings.getInstance()
        pathsListModel?.let { model ->
            settings.projectsRootPaths = model.items.toMutableList()
        }
        forceNewWindowCheckbox?.let { checkbox ->
            settings.forceOpenInNewWindow = checkbox.isSelected
        }
    }

    override fun reset() {
        val settings = ProjectOpenerSettings.getInstance()
        pathsListModel?.replaceAll(settings.projectsRootPaths.toMutableList())
        forceNewWindowCheckbox?.isSelected = settings.forceOpenInNewWindow
    }

    override fun disposeUIResources() {
        pathsListModel = null
        mainPanel = null
        forceNewWindowCheckbox = null
    }
}
