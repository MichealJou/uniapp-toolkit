package com.greenleafcat.uniapptoolkit.components

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComboBox
import javax.swing.JComponent

class TemplateEditorDialog(project: Project, private val config: UniappRunConfiguration) : DialogWrapper(project) {

    private val nameField = JBTextField(config.name)
    private val typeComboBox = JComboBox(arrayOf("h5", "app-plus", "mp-weixin", "mp-alipay", "mp-baidu", "mp-toutiao"))
    private val portField = JBTextField(config.port.toString())
    private val commandField = JBTextField(config.command)
    private val workingDirField = JBTextField(config.workingDirectory)

    init {
        typeComboBox.selectedItem = config.type
        title = "Uniapp Run Configuration"
        init()
    }

    override fun createCenterPanel(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Name:"), nameField)
            .addLabeledComponent(JBLabel("Type:"), typeComboBox)
            .addLabeledComponent(JBLabel("Port:"), portField)
            .addLabeledComponent(JBLabel("Command:"), commandField)
            .addLabeledComponent(JBLabel("Working Directory:"), workingDirField)
            .panel
    }

    fun getConfiguration(): UniappRunConfiguration {
        return config.copy(
            name = nameField.text,
            type = typeComboBox.selectedItem.toString(),
            port = portField.text.toIntOrNull() ?: 8080,
            command = commandField.text,
            workingDirectory = workingDirField.text
        )
    }
}
