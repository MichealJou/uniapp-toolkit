package com.greenleafcat.uniapptoolkit.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent
import javax.swing.JPanel

class UniAppSettingsComponent {

    val panel: JPanel
    private val myComment = JBLabel("Future settings for the Uni-app plugin will appear here.")

    init {
        panel = panel {
            row {
                cell(myComment)
            }
        }
    }

    val preferredFocusedComponent: JComponent
        get() = myComment
}
