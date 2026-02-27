package com.makoto.projectopener

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service
@State(
    name = "ProjectOpener",
    storages = [Storage("projectOpener.xml")]
)
final class ProjectOpenerSettings : SimplePersistentStateComponent<ProjectOpenerSettings.State>(State()) {

    class State : BaseState() {
        var projectsRootPaths by list<String>()
        var forceOpenInNewWindow by property(false)
    }

    var projectsRootPaths: MutableList<String>
        get() = state.projectsRootPaths
        set(value) {
            state.projectsRootPaths = value
        }

    var forceOpenInNewWindow: Boolean
        get() = state.forceOpenInNewWindow
        set(value) {
            state.forceOpenInNewWindow = value
        }

    companion object {
        fun getInstance(): ProjectOpenerSettings =
            com.intellij.openapi.application.ApplicationManager.getApplication().getService(ProjectOpenerSettings::class.java)
    }
}
