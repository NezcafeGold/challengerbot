package com.nzqk.challengerbot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.nzqk.challengerbot.command.CreateTaskCommand
import com.nzqk.challengerbot.command.GetTaskCommand
import com.nzqk.challengerbot.utils.ApplicationContextUtils
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

            }

//            command("get_task") {
//                val getTaskCommand = appCtx.getBean("getTaskCommand", GetTaskCommand::class.java)
//                val task = getTaskCommand.execute(message)
//                val message = "${task.id}: ${task.title ?: "Без названия"}\n" +
//                        "Владелец: ${task.owner?.name}" +
//                        "${task.description ?: ""}\n"
//                sendMessage(message)
//
//            }

            command("help") {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = " Возможные команды",
                    replyToMessageId = update.message!!.messageId,
                    replyMarkup = InlineKeyboardMarkup.createSingleRowKeyboard(
                        InlineKeyboardButton.CallbackData(
                            "Создать задачу",
                            "create_task"
                        ),
                        InlineKeyboardButton.CallbackData(
                            "Мои задачи",
                            "my_tasks"
                        )
                    )

                )

            }

            callbackQuery("create_task") {
                val createTaskCommand = appCtx.getBean("createTaskCommand", CreateTaskCommand::class.java)
                val task = createTaskCommand.execute(callbackQuery.message!!)
                val text =
                    "Для @${callbackQuery.from.username} успешно создана задач с номером ${task.id}"
                sendMessage(text)
            }

            callbackQuery("my_tasks") {
                bot.answerCallbackQuery(
                    "BYE",
                    "SSS"
                )
            }
        }
    }
    bot.startPolling()
}

private fun CallbackQueryHandlerEnvironment.sendMessage(response: String) {
    bot.sendMessage(
        chatId = ChatId.fromId(callbackQuery.message!!.chat.id),
        text = response,
        replyToMessageId = callbackQuery.message!!.messageId
    )
}