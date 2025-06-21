package com.greenleafcat.uniapptoolkit.configurations

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import javax.swing.Icon

class UniappRunConfigurationType : ConfigurationType {
    companion object {
        private val INSTANCE = UniappRunConfigurationType()

        fun getInstance(): UniappRunConfigurationType = INSTANCE
    }

    override fun getDisplayName(): String = "Uniapp"

    override fun getConfigurationTypeDescription(): String = "Uniapp Development Configuration"

    override fun getIcon(): Icon = PluginIcons.UNIAPP

    override fun getId(): String = "UniappRunConfiguration"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(UniappConfigurationFactory(this))
    }
}


class UniappConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getName(): String = "Uniapp"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return UniappRunConfigurationImpl(project, this, "Uniapp Configuration")
    }
}
