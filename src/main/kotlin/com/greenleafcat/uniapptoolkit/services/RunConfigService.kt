package com.greenleafcat.uniapptoolkit.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

/**
 * Run config service
 * @author Michael Jou
 * @property project
 * @constructor Create empty Run config service
 */
@Service
class RunConfigService(private val project: Project) {
    companion object {
        fun getInstance(project: Project): RunConfigService =
            project.getService(RunConfigService::class.java)
    }

    fun generateTemplate(packageJson: Map<String, Any>?): UniappRunConfiguration {
        val generator = RunConfigGenerator(packageJson)
        return generator.generate()
    }

    fun showConfigurationDialog(template: UniappRunConfiguration): UniappRunConfiguration? {
        val dialog = TemplateEditorDialog(project, template)
        return if (dialog.showAndGet()) {
            dialog.getConfiguration()
        } else {
            null
        }
    }

    // 保存配置状态
    fun saveConfiguration(config: UniappRunConfiguration) {
        // 将配置保存到项目文件中
        val configFile = project.baseDir?.findChild(".idea")?.findChild("uniapp-config.json")
        // 写入配置...
    }

    // 加载保存的配置
    fun loadSavedConfiguration(): UniappRunConfiguration? {
        // 从项目文件加载配置
        return null
    }
}
