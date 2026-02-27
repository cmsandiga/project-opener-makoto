package com.makoto.projectopener

import java.io.File

data class ProjectInfo(
    val name: String,
    val path: String
) {
    fun displayText(): String {
        val parentPath = File(path).parent ?: path
        return "$name ($parentPath)"
    }
}
