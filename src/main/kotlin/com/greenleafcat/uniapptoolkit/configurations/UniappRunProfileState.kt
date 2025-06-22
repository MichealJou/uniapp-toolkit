package com.greenleafcat.uniapptoolkit.configurations

import com.greenleafcat.uniapptoolkit.models.UniappRunConfiguration
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import kotlin.coroutines.ContinuationInterceptor

class UniappRunProfileState(environment: ExecutionEnvironment, private val config: UniappRunConfiguration) :
    CommandLineState(environment) {
    private val project: Project = environment.project

    override fun startProcess(): ProcessHandler {
        val commandLine = createCommandLine()
        val processHandler = ColoredProcessHandler(commandLine)

        // 添加进程监听器，用于监控进程状态
        processHandler.addProcessListener(object : ProcessAdapter() {
            override fun processTerminated(event: ProcessEvent) {
                // 进程终止时的处理
            }

            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                super.onTextAvailable(event, outputType)
               // 输出文本时的处理
            }

        })

        return processHandler
    }

    private fun createCommandLine(): GeneralCommandLine {
        val commandLine = GeneralCommandLine()

        // 设置工作目录
        commandLine.workDirectory = config.workingDirectory

        // 拆分命令为程序和参数
        var commandParts = splitCommand(config.command)
        if (commandParts.isNotEmpty()) {
            commandLine.exePath = commandParts[0]
            if (commandParts.size > 1) {
                commandLine.addParameters(commandParts.subList(1, commandParts.size))
            }
        }

        // 添加环境变量
        val environment = mutableMapOf<String, String>()
        environment.putAll(System.getenv())
        environment.putAll(config.env)
        commandLine.environment = environment

        return commandLine
    }

    private fun splitCommand(command: String): List<String> {
        // 简单的命令拆分实现，处理带引号的参数
        val parts = mutableListOf<String>()
        var currentPart = StringBuilder()
        var inQuotes = false

        for (char in command) {
            when {
                char == '"' -> inQuotes = !inQuotes
                char == ' ' && !inQuotes -> {
                    if (currentPart.isNotEmpty()) {
                        parts.add(currentPart.toString())
                        currentPart = StringBuilder()
                    }
                }

                else -> currentPart.append(char)
            }
        }

        if (currentPart.isNotEmpty()) {
            parts.add(currentPart.toString())
        }

        return parts
    }

    override fun createConsole(executor: Executor): ConsoleView {
        return TextConsoleBuilderFactory.getInstance().createBuilder(project).console
    }
}
