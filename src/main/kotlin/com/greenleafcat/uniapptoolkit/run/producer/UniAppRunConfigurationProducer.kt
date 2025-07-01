package com.greenleafcat.uniapptoolkit.run.producer

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.greenleafcat.uniapptoolkit.run.UniAppRunConfiguration
import com.greenleafcat.uniapptoolkit.run.UniAppRunConfigurationType
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.PsiManager

class UniAppRunConfigurationProducer : LazyRunConfigurationProducer<UniAppRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return UniAppRunConfigurationType().configurationFactories[0]
    }

    override fun isConfigurationFromContext(
        configuration: UniAppRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val scriptName = getScriptNameFromContext(context)
        return scriptName != null && configuration.npmRunSettings?.scripts?.contains(scriptName) == true
    }

    override fun setupConfigurationFromContext(
        configuration: UniAppRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val scriptName = getScriptNameFromContext(context)
        if (scriptName != null) {
            configuration.name = "Run '" + scriptName + "'"
            configuration.npmRunSettings = configuration.npmRunSettings?.withScripts(listOf(scriptName))
            return true
        }
        return false
    }

    private fun getScriptNameFromContext(context: ConfigurationContext): String? {
        val psiElement = context.psiLocation ?: return null
        val packageJson = findPackageJson(psiElement) ?: return null

        val scripts = findScripts(packageJson) ?: return null

        for (property in scripts.propertyList) {
            if (isUniAppScript(property.name)) {
                return property.name
            }
        }
        return null
    }

    private fun findPackageJson(element: PsiElement): VirtualFile? {
        val containingFile = element.containingFile
        if (containingFile != null && containingFile.name == "package.json") {
            return containingFile.virtualFile
        }
        return null
    }

    private fun findScripts(packageJsonFile: VirtualFile): JsonObject? {
        val psiFile = PsiTreeUtil.findChildOfType(packageJsonFile.psiFile, JsonFile::class.java)
        val topLevelObject = PsiTreeUtil.findChildOfType(psiFile, JsonObject::class.java)
        val scriptsProperty = topLevelObject?.findProperty("scripts")
        return scriptsProperty?.value as? JsonObject
    }

    private fun isUniAppScript(scriptName: String): Boolean {
        return scriptName.startsWith("dev:mp-") || scriptName.startsWith("build:mp-")
    }
}

private val VirtualFile.psiFile
    get() = PsiManager.getInstance(ProjectManager.getInstance().openProjects[0]).findFile(this)
