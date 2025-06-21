package com.greenleafcat.uniapptoolkit.configurations

import com.greenleafcat.uniapptoolkit.models.UniappRunConfigurationImpl
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent

class UniappRunConfigurationEditor(project: Project): SettingsEditor<UniappRunConfigurationImpl> {
    private val nameField = JBTextField()
    private val typeComboBox = JBComboBox(arrayOf("h5", "app-plus", "mp-weixin", "mp-alipay", "mp-baidu", "mp-toutiao"))
    private val portField = JBTextField()
    private val commandField = JBTextField()
    private val workingDirField = JBTextField()

    override fun createEditor(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Name:"), nameField)
            .addLabeledComponent(JBLabel("Platform:"), typeComboBox)
            .addLabeledComponent(JBLabel("Port:"), portField)
            .addLabeledComponent(JBLabel("Command:"), commandField)
            .addLabeledComponent(JBLabel("Working Directory:"), workingDirField)
            .addComponentFillVertically(JBLabel(""), 0)
            .panel
    }

    override fun resetEditorFrom(config: UniappRunConfigurationImpl) {
        nameField.text = config.name
        typeComboBox.selectedItem = config.config.type
        portField.text = config.config.port.toString()
        commandField.text = config.config.command
        workingDirField.text = config.config.workingDirectory
    }

    override fun applyEditorTo(config: UniappRunConfigurationImpl) {
        config.name = nameField.text
        config.config.type = typeComboBox.selectedItem.toString()
        config.config.port = portField.text.toIntOrNull() ?: 8080
        config.config.command = commandField.text
        config.config.workingDirectory = workingDirField.text
    }
}
