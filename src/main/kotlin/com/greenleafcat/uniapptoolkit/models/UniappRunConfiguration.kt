package com.greenleafcat.uniapptoolkit.models

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import kotlin.coroutines.CoroutineContext

data class UniappRunConfiguration(
    val name: String,
    var type: String = "h5",
    var port: Int = 8080,
    var env: MutableMap<String, String> = mutableMapOf(),
    var command: String = "npm run dev:h5",
    var workingDirectory: String = "",
    var beforeRunTasks: MutableList<String> = mutableListOf()
) {
    companion object {
        fun getDefault(project: Project): UniappRunConfiguration {
            return UniappRunConfiguration(
                name = "Uniapp Development",
                workingDirectory = project.basePath ?: ""
            )
        }
    }

}

class UniappRunConfigurationImpl(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<UniappRunConfiguration>(project, factory, name) {
var config = UniappRunConfiguration(name)

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return UniappRunConfigurationEditor(project)
    }

    override fun getState(env: ExecutionEnvironment, state: RunProfileState): RunProfileState? {
        // 配置执行环境
        return UniappRunProfileState(env, config)
    }

    override fun writeExternal(element: CoroutineContext.Element) {
        super.writeExternal(element)
        // 保存配置到 XML
        element.setAttribute("type", config.type)
        element.setAttribute("port", config.port.toString())
        // 保存其他配置...
    }

    override fun readExternal(element: CoroutineContext.Element) {
        super.readExternal(element)
        // 从 XML 读取配置
        config.type = element.getAttributeValue("type") ?: "h5"
        config.port = element.getAttributeValue("port")?.toIntOrNull() ?: 8080
        // 读取其他配置...
    }
}
