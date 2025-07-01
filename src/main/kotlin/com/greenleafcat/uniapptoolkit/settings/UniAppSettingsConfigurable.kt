package com.greenleafcat.uniapptoolkit.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class UniAppSettingsConfigurable : Configurable {

    private var mySettingsComponent: UniAppSettingsComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return "Uni-app Support"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return mySettingsComponent!!.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = UniAppSettingsComponent()
        return mySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        // Add logic to check if settings are modified
        return false
    }

    override fun apply() {
        // Add logic to save settings
    }

    override fun reset() {
        // Add logic to reset settings
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
