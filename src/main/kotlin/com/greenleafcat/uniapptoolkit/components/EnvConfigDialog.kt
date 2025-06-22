package com.greenleafcat.uniapptoolkit.components

import com.greenleafcat.uniapptoolkit.services.EnvConfigService
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class EnvConfigDialog(project: Project?) : DialogWrapper(project) {
    private val envService = EnvConfigService.getInstance(project!!)
    private val envList = JBList(envService.getEnvironments().keys.toList())
    private val varTableModel = createVarTableModel(emptyList())
    private val varTable = JBTable(varTableModel)

    init {
        title = "Uniapp Environment Configuration"
        init()

        // 监听环境选择变化
        envList.selectionModel.addListSelectionListener(object : ListSelectionListener {
            override fun valueChanged(e: ListSelectionEvent) {
                if (!e.valueIsAdjusting) {
                    val selectedEnv = envList.selectedValue as? String
                    if (selectedEnv != null) {
                        updateVarTable(selectedEnv)
                    }
                }
            }
        })

        // 选择第一个环境
        if (envList.itemsCount > 0) {
            envList.selectedIndex = 0
        }
    }

    private fun createVarTableModel(initialData: List<Pair<String, String>>): ListTableModel<Pair<String, String>> {
        val columns = arrayOf(
            object : ColumnInfo<Pair<String, String>, String>("Variable") {
                override fun valueOf(item: Pair<String, String>): String = item.first
                override fun setValue(item: Pair<String, String>, value: String) {
                    item as MutablePair
                    item.first = value
                }
                override fun isCellEditable(item: Pair<String, String>): Boolean = true
            },
            object : ColumnInfo<Pair<String, String>, String>("Value") {
                override fun valueOf(item: Pair<String, String>): String = item.second
                override fun setValue(item: Pair<String, String>, value: String) {
                    item as MutablePair
                    item.second = value
                }
                override fun isCellEditable(item: Pair<String, String>): Boolean = true
            }
        )

        return ListTableModel(columns, initialData.toMutableList())
    }

    private fun updateVarTable(envName: String) {
        val variables = envService.getEnvironment(envName) ?: emptyMap()
        val data = variables.entries.map { (key, value) -> MutablePair(key, value) }.toMutableList()
        varTableModel.items = data
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())

        // 左侧环境列表
        val envListPanel = ToolbarDecorator.createDecorator(envList)
            .setAddAction { addEnvironment() }
            .setRemoveAction { removeEnvironment() }
            .createPanel()

        // 右侧变量表格
        val varTablePanel = ToolbarDecorator.createDecorator(varTable)
            .setAddAction { addVariable() }
            .setRemoveAction { removeVariable() }
            .createPanel()

        // 分割面板
        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, envListPanel, varTablePanel)
        splitPane.dividerLocation = 200

        panel.add(splitPane, BorderLayout.CENTER)
        return panel
    }

    private fun addEnvironment() {
        val envName = JOptionPane.showInputDialog(
            this.rootPane,
            "Enter environment name:",
            "New Environment",
            JOptionPane.PLAIN_MESSAGE
        )

        if (envName != null && envName.isNotBlank()) {
            envService.createEnvironment(envName, mutableMapOf())
            updateEnvList()
            envList.selectedValue = envName
        }
    }

    private fun removeEnvironment() {
        val selectedEnv = envList.selectedValue as? String ?: return
        envService.deleteEnvironment(selectedEnv)
        updateEnvList()
    }

    private fun addVariable() {
        val selectedEnv = envList.selectedValue as? String ?: return
        val varName = JOptionPane.showInputDialog(
            this.rootPane,
            "Enter variable name:",
            "New Variable",
            JOptionPane.PLAIN_MESSAGE
        )

        if (varName != null && varName.isNotBlank()) {
            val varValue = JOptionPane.showInputDialog(
                this.rootPane,
                "Enter variable value:",
                "New Variable",
                JOptionPane.PLAIN_MESSAGE
            )

            if (varValue != null) {
                val variables = envService.getEnvironment(selectedEnv)?.toMutableMap() ?: mutableMapOf()
                variables[varName] = varValue
                envService.updateEnvironment(selectedEnv, variables)
                updateVarTable(selectedEnv)
            }
        }
    }

    private fun removeVariable() {
        val selectedEnv = envList.selectedValue as? String ?: return
        val selectedRow = varTable.selectedRow

        if (selectedRow >= 0) {
            val variables = envService.getEnvironment(selectedEnv)?.toMutableMap() ?: mutableMapOf()
            val varName = varTableModel.getItem(selectedRow).first
            variables.remove(varName)
            envService.updateEnvironment(selectedEnv, variables)
            updateVarTable(selectedEnv)
        }
    }

    private fun updateEnvList() {
        envList.setListData(envService.getEnvironments().keys.toTypedArray())
    }

    override fun doOKAction() {
        // 保存当前环境变量
        val selectedEnv = envList.selectedValue as? String ?: return
        val variables = mutableMapOf<String, String>()

        for (i in 0 until varTableModel.rowCount) {
            val item = varTableModel.getItem(i)
            variables[item.first] = item.second
        }

        envService.updateEnvironment(selectedEnv, variables)
        super.doOKAction()
    }

    data class MutablePair(var first: String, var second: String)
}
