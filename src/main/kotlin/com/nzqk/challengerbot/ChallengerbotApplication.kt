package com.nzqk.challengerbot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import com.nzqk.challengerbot.command.CreateTaskCommand
import com.nzqk.challengerbot.command.GetTaskCommand
import com.nzqk.challengerbot.utils.ApplicationContextUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext


@SpringBootApplication
open class ChallengerbotApplication {
    @Autowired
    lateinit var createTaskCommand: CreateTaskCommand
}


fun main(args: Array<String>) {

    runApplication<ChallengerbotApplication>(*args)
    val appCtx: ApplicationContext = ApplicationContextUtils.applicationContext!!
    val bot = bot {
        token = "5393101475:AAG4uNHFuKBTO_hNKRCcBOHct3QF9EvQWEE"
        dispatch {

            command("create_task") {
                val createTaskCommand = appCtx.getBean("createTaskCommand", CreateTaskCommand::class.java)
                val task = createTaskCommand.execute(message)
                val message =
                    "Для тебя успешно создана задач с номером ${task.id}"
                sendMessage(message)
            }

            command("get_task") {
                val getTaskCommand = appCtx.getBean("getTaskCommand", GetTaskCommand::class.java)
                val task = getTaskCommand.execute(message)
                val message = "${task.id}: ${task.title ?: "Без названия"}\n" +
                        "Владелец: ${task.owner?.name}" +
                        "${task.description ?: ""}\n"
                sendMessage(message)
            }

        }
    }
    bot.startPolling()
}

private fun CommandHandlerEnvironment.sendMessage(response: String) {
    bot.sendMessage(
        chatId = ChatId.fromId(message.chat.id),
        text = response,
        replyToMessageId = update.message!!.messageId
    )
}