package com.greenleafcat.uniapptoolkit.actions

import com.greenleafcat.uniapptoolkit.components.EnvConfigDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

/**
 * Open env config action
 * @author Michael Jou
 * @constructor Create empty Open env config action
 */
class OpenEnvConfigAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val dialog = EnvConfigDialog(project)
        dialog.show()
    }
}
