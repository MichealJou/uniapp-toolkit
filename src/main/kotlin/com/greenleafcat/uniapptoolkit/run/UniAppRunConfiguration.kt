package com.greenleafcat.uniapptoolkit.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.javascript.nodejs.npm.NpmRunConfiguration
import com.intellij.execution.javascript.nodejs.npm.NpmRunSettings
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element

class UniAppRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, factory, name), NpmRunConfiguration {

    private var scriptName: String? = null

    override fun getSettingsEditor(): SettingsEditor<out RunConfiguration> {
        // For this implementation, we reuse the Npm run configuration editor.
        // A custom editor can be created if more specific settings are needed.
        return NpmRunConfiguration.createSettingsEditor(project, this)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        val npmSettings = npmRunSettings
        if (npmSettings != null) {
            return NpmRunConfiguration.createNpmRunningState(environment, npmSettings)
        }
        return null
    }

    override fun getConfigurationEditor(): SettingsEditor<UniAppRunConfiguration> {
        return NpmRunConfiguration.createSettingsEditor(project, this)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        scriptName = JDOMExternalizerUtil.readField(element, "scriptName")
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, "scriptName", scriptName)
    }

    override var npmRunSettings: NpmRunSettings?
        get() = NpmRunSettings.create(project, scriptName)
        set(value) {
            scriptName = value?.scripts?.firstOrNull()
        }
}
