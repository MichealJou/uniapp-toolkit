package com.greenleafcat.uniapptoolkit.actions

import com.greenleafcat.uniapptoolkit.configurations.UniappRunConfigurationType
import com.greenleafcat.uniapptoolkit.generators.PackageJsonParser
import com.greenleafcat.uniapptoolkit.services.RunConfigService
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

/**
 * Create uniapp run config action
 * @author Michael Jou
 * @constructor Create empty Create uniapp run config action
 */
class CreateUniappRunConfigAction : AnAction(), DumbAware {
     override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val runConfigService = RunConfigService.Companion.getInstance(project)
        val parser = PackageJsonParser(project)

        // 从 package.json 生成配置模板
        val packageJson = parser.parse()
        val template = runConfigService.generateTemplate(packageJson)

        // 显示配置对话框
        val config = runConfigService.showConfigurationDialog(template) ?: return

        // 创建并保存运行配置
        val factory: ConfigurationFactory = UniappRunConfigurationType.getInstance().factory
        val runConfiguration = factory.createTemplateConfiguration(project)
        runConfiguration.name = config.name
        // 设置其他配置属性...

        // 添加到运行管理器
        RunManager.getInstance(project).addConfiguration(runConfiguration)
    }

}
