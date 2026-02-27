package com.makoto.projectopener

import com.intellij.openapi.components.Service
import java.io.File

@Service
final class ProjectDiscoveryService {

    private val projectIndicators = listOf(
        ".idea",
        "build.gradle",
        "build.gradle.kts",
        "pom.xml",
        "settings.gradle",
        "settings.gradle.kts"
    )

    fun findProjects(searchPattern: String): List<ProjectInfo> {
        val settings = ProjectOpenerSettings.getInstance()
        val rootPaths = settings.projectsRootPaths
        if (rootPaths.isEmpty()) return emptyList()

        val allProjects = mutableListOf<ProjectInfo>()
        for (rootPath in rootPaths) {
            val rootDir = File(rootPath)
            if (!rootDir.exists() || !rootDir.isDirectory) continue
            scanForProjects(rootDir, rootDir, allProjects)
        }

        val pattern = searchPattern.trim().lowercase()

        return allProjects
            .distinctBy { it.path }
            .filter { project ->
                pattern.isEmpty() ||
                    project.name.lowercase().contains(pattern) ||
                    project.path.lowercase().contains(pattern)
            }
            .sortedBy { it.name.lowercase() }
    }

    private fun scanForProjects(rootDir: File, currentDir: File, result: MutableList<ProjectInfo>, depth: Int = 0) {
        if (currentDir != rootDir && isProjectDirectory(currentDir)) {
            result.add(ProjectInfo(name = currentDir.name, path = currentDir.absolutePath))
            return
        }
        if (depth >= 3) return

        val children = currentDir.listFiles() ?: return
        for (child in children) {
            if (!child.isDirectory) continue
            if (child.name.startsWith(".") && child.name != ".idea") continue
            if (child.name in listOf("node_modules", "build", "target", ".git")) continue

            scanForProjects(rootDir, child, result, depth + 1)
        }
    }

    private fun isProjectDirectory(dir: File): Boolean {
        for (indicator in projectIndicators) {
            val file = File(dir, indicator)
            if (file.exists()) return true
        }
        return false
    }

    companion object {
        fun getInstance(): ProjectDiscoveryService =
            com.intellij.openapi.application.ApplicationManager.getApplication().getService(ProjectDiscoveryService::class.java)
    }
}
