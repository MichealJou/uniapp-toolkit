package com.greenleafcat.uniapptoolkit.services

import com.greenleafcat.uniapptoolkit.generators.RunConfigGenerator
import com.greenleafcat.uniapptoolkit.models.UniappRunConfiguration
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.google.gson.Gson
import com.greenleafcat.uniapptoolkit.generators.PackageJsonParser


/**
 * Run config service
 * @author Michael Jou
 * @property project
 * @constructor Create empty Run config service
 */
@Service
@State(
    name = "UniappRunConfigService", storages = [Storage("uniapp-run-config.xml")]
)
class RunConfigService(private val project: Project) : PersistentStateComponent<RunConfigService.State> {

    data class State(
        var configurations: MutableList<UniappRunConfiguration> = mutableListOf(), var lastUsedConfig: String? = null
    )

    private var state = State()

    companion object {
        fun getInstance(project: Project): RunConfigService = project.getService(RunConfigService::class.java)
    }

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun generateTemplate(packageJson1: Map<String, Any>?): UniappRunConfiguration {
        val parser = PackageJsonParser(project)
        val packageJson = parser.parse()
        val generator = RunConfigGenerator(packageJson)
        return generator.generate()
    }

    fun saveConfiguration(config: UniappRunConfiguration) {
        // 查找是否已存在同名配置
        val existingIndex = state.configurations.indexOfFirst { it.name == config.name }

        if (existingIndex >= 0) {
            state.configurations[existingIndex] = config
        } else {
            state.configurations.add(config)
        }

        state.lastUsedConfig = config.name
    }

    fun getSavedConfigurations(): List<UniappRunConfiguration> {
        return state.configurations
    }

    fun getLastUsedConfiguration(): UniappRunConfiguration? {
        return state.lastUsedConfig?.let { name ->
            state.configurations.find { it.name == name }
        } ?: state.configurations.firstOrNull()
    }

    fun exportConfiguration(config: UniappRunConfiguration): String {
        return Gson().toJson(config)
    }

    fun importConfiguration(json: String): UniappRunConfiguration? {
        return try {
            Gson().fromJson(json, UniappRunConfiguration::class.java)
        } catch (e: Exception) {
            null
        }
    }

}
