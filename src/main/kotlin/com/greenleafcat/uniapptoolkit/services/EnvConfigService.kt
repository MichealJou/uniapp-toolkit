package com.greenleafcat.uniapptoolkit.services

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service
@State(
    name = "UniappEnvConfigService", storages = [Storage("uniapp-env-config.xml")]
)
class EnvConfigService(private val project: Project) : PersistentStateComponent<EnvConfigService.State> {
    data class State(
        var environments: MutableMap<String, MutableMap<String, String>> = mutableMapOf(),
        var activeEnv: String = "development"
    )

    private var state = State().apply {
        // 添加默认环境
        if (!environments.containsKey("development")) {
            environments["development"] = mutableMapOf(
                "NODE_ENV" to "development", "VUE_APP_MODE" to "dev", "VUE_APP_API_URL" to "http://localhost:3000/api"
            )
        }

        if (!environments.containsKey("production")) {
            environments["production"] = mutableMapOf(
                "NODE_ENV" to "production", "VUE_APP_MODE" to "prod", "VUE_APP_API_URL" to "https://api.example.com"
            )
        }
    }

    companion object {
        fun getInstance(project: Project): EnvConfigService = project.getService(EnvConfigService::class.java)
    }

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun getEnvironments(): Map<String, Map<String, String>> {
        return state.environments
    }

    fun getEnvironment(name: String): Map<String, String>? {
        return state.environments[name]
    }

    fun getActiveEnvironment(): Map<String, String>? {
        return getEnvironment(state.activeEnv)
    }

    fun setActiveEnvironment(name: String) {
        if (state.environments.containsKey(name)) {
            state.activeEnv = name
        }
    }

    fun createEnvironment(name: String, variables: Map<String, String>) {
        state.environments[name] = variables.toMutableMap()
    }

    fun updateEnvironment(name: String, variables: Map<String, String>) {
        state.environments[name] = variables.toMutableMap()
    }

    fun deleteEnvironment(name: String) {
        state.environments.remove(name)
        if (state.activeEnv == name && state.environments.isNotEmpty()) {
            state.activeEnv = state.environments.keys.first()
        }
    }
}
