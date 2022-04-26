package com.nzqk.challengerbot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.network.fold
import com.github.kotlintelegrambot.webhook
import com.nzqk.challengerbot.command.CreateTaskCommand
import com.nzqk.challengerbot.utils.ApplicationContextUtils
import com.nzqk.challengerbot.utils.ReplyHandler
import org.aspectj.bridge.MessageHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext


@SpringBootApplication
open class ChallengerbotApplication

private enum class CommandMessage(val message: String) {
    ADD_TITLE("Добавить заголовок"),
    ADD_DESCRIPTION("Добавить описание")
}

fun main(args: Array<String>) {
    runApplication<ChallengerbotApplication>(*args)

    val regexpCommandMessage = "^[а-яА-Я]+\\s[а-я]+\\s\\[\\d+]\$".toRegex()
    val appCtx: ApplicationContext = ApplicationContextUtils.applicationContext!!
    val bot = bot {
        token = "5393101475:AAG4uNHFuKBTO_hNKRCcBOHct3QF9EvQWEE"
        dispatch {

            command("create_task") {

            }

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

//                val task = createTaskCommand.execute(callbackQuery.message!!)
//                val text =
//                    "Для @${callbackQuery.from.username} успешно создана задач с номером ${task.id}!"

//                replyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(
//                    listOf(
//                        listOf("${CommandMessage.ADD_TITLE.message} [${task.id}]"),
//                        listOf("${CommandMessage.ADD_DESCRIPTION.message} [${task.id}]"),
//                    )
//                )

                val text = "Задай название для задачи"

                val titleResult = bot.sendMessage(
                    chatId = ChatId.fromId(callbackQuery.message!!.chat.id),
                    text = text,
                    replyToMessageId = callbackQuery.message!!.messageId,
                    replyMarkup = ForceReplyMarkup(true, selective = true)
                )


                var replyHandler: ReplyHandler? = null
                fun remove() {
                    replyHandler?.let { removeHandler(it) }
                }
                titleResult.fold({
                    replyHandler = ReplyHandler(Filter.Reply) {
                        println("sasd")
                        remove()
                    }
                    addHandler(replyHandler!!)
                }, {

                })


            }

            message {
                if (message.text?.matches(regexpCommandMessage) == true) {
//                    message.text.split(" ").also {
//                        it[2]
//                    }
// делаем проверку на возможность использования данной задачи
                    val result = bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "SS",
                        replyToMessageId = message.messageId,
                        replyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(
                            listOf(
//                                listOf("${CommandMessage.ADD_TITLE.message} [${task.id}]"),
//                                listOf("${CommandMessage.ADD_DESCRIPTION.message} [${task.id}]"),
                            )
                        )
                    )
                    result.fold({

                    }, {

                    })
                }
            }
        }
    }
    bot.startPolling()
}
