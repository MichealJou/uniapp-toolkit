package com.greenleafcat.uniapptoolkit.generators

import com.google.gson.Gson
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.BufferedReader
import java.io.InputStreamReader

class PackageJsonParser(private val project: Project) {
    fun parse(): Map<String, Any>? {
        val packageJsonFile = findPackageJsonFile() ?: return null

        return try {
            val reader = BufferedReader(InputStreamReader(packageJsonFile.inputStream))
            val gson = Gson()
            @Suppress("UNCHECKED_CAST")
            gson.fromJson(reader, Map::class.java) as Map<String, Any>
        } catch (e: Exception) {
            null
        }
    }

    private fun findPackageJsonFile(): VirtualFile? {
        val baseDir = project.baseDir ?: return null
        return baseDir.findChild("package.json")
    }
}
