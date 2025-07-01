package com.greenleafcat.uniapptoolkit.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.yourcompany.uniapp_support.icons.UniAppIcons
import javax.swing.Icon

class UniAppRunConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = "Uni-app"

    override fun getConfigurationTypeDescription(): String = "Uni-app run configuration type"

    override fun getIcon(): Icon = UniAppIcons.UNI_APP_ICON

    override fun getId(): String = "UniAppRunConfiguration"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(UniAppConfigurationFactory(this))
    }
}

class UniAppConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return UniAppRunConfiguration(project, this, "Uni-app")
    }

    override fun getId(): String = "UniAppRunConfigurationFactory"

    override fun getName(): String = "Uni-app factory"
}
