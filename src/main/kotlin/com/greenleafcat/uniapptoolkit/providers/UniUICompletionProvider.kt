package com.greenleafcat.uniapptoolkit.providers

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext

/**
 * Uni u i completion provider
 *@author Michael Jou
 * @constructor Create empty Uni u i completion provider
 */
class UniUICompletionProvider : CompletionContributor() {
    // UniUI 组件列表
    private val UNIUI_COMPONENTS = listOf(
        "uni-button",
        "uni-badge",
        "uni-card",
        "uni-collapse",
        "uni-datetime-picker",
        "uni-forms",
        "uni-grid",
        "uni-input",
        "uni-load-more",
        "uni-nav-bar",
        "uni-notice-bar",
        "uni-popup",
        "uni-progress",
        "uni-radio",
        "uni-search-bar",
        "uni-section",
        "uni-steps",
        "uni-switch",
        "uni-tab-bar",
        "uni-tabs",
        "uni-tag",
        "uni-textarea",
        "uni-transition"
    )

    // 组件属性映射
    private val COMPONENT_ATTRS = mapOf(
        "uni-button" to listOf("type", "size", "plain", "disabled", "loading", "form-type"),
        "uni-input" to listOf("type", "value", "placeholder", "disabled", "maxlength", "password"),
        "uni-tabs" to listOf("current", "color", "background", "border", "scrollable"),
        "uni-popup" to listOf("type", "show", "mask", "mask-close-able", "close-on-click-modal"),
        "uni-datetime-picker" to listOf("type", "start", "end", "fields", "value")
        // 其他组件属性...
    )

    init {
        // 补全 HTML 标签名
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(XmlTag::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                ) {
                    UNIUI_COMPONENTS.forEach { component ->
                        result.addElement(LookupElementBuilder.create(component))
                    }
                }
            })

        // 补全组件属性
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withSuperParent(2, XmlTag::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                ) {
                    val xmlTag = parameters.position.parent.parent as? XmlTag ?: return
                    val tagName = xmlTag.name

                    COMPONENT_ATTRS[tagName]?.forEach { attr ->
                        result.addElement(LookupElementBuilder.create(attr))
                    }
                }
            })
    }
}
