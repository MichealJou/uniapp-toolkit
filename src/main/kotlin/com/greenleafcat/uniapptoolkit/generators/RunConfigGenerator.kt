package com.greenleafcat.uniapptoolkit.generators

import com.greenleafcat.uniapptoolkit.models.UniappRunConfiguration

/**
 * RunConfigGenerator
 * @author Michael Jou
 * @constructor Create empty Run config generator
 * Created by greenleaf on 2017/5/23.
 */
class RunConfigGenerator(private val packageJson: Map<String, Any>?) {

    companion object {
        private const val DEFAULT_PORT = 8080;
        private const val DEFAULT_TYPE = "h5";
    }

    fun generate(): UniappRunConfiguration {
        val name = getAppName()
        val type = DEFAULT_TYPE
        val port = getPort()
        val command = getCommand()
        val env = getEnvVariables()

        return UniappRunConfiguration(
            name = name, type = type, port = port, command = command, env = env
        )
    }

    private fun getAppName(): String {
        val name = packageJson?.get("name") as? String ?: "Uniapp"
        return "Uniapp - $name"
    }

    private fun getPort(): Int {
        // 从 package.json 或其他配置中提取端口
        return DEFAULT_PORT
    }

    private fun getCommand(): String {
        val scripts = packageJson?.get("scripts") as? Map<*, *>

        return when {
            scripts?.containsKey("dev:h5") == true -> "npm run dev:h5"
            scripts?.containsKey("dev") == true -> "npm run dev"
            else -> "npx uni-app-cli dev"
        }
    }

    private fun getEnvVariables(): MutableMap<String, String> {
        val env = mutableMapOf<String, String>()
        env["NODE_ENV"] = "development"
        env["UNI_PLATFORM"] = DEFAULT_TYPE

        // 可以从 package.json 或其他配置中提取更多环境变量

        return env
    }
}
