package com.nzqk.challengerbot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ForceReplyMarkup
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.network.fold
import com.nzqk.challengerbot.command.CreateTaskCommand
import com.nzqk.challengerbot.observer.MessageObserver
import com.nzqk.challengerbot.utils.ApplicationContextUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext


@SpringBootApplication
class ChallengerbotApplication {

    val appCtx: ApplicationContext by lazy { ApplicationContextUtils.applicationContext!! }
    val createTaskCommand by lazy { appCtx.getBean("createTaskCommand", CreateTaskCommand::class.java) }

    fun start() {

        val observer = MessageObserver()
        observer.startTimer()

        val regexpCommandMessage = "^[а-яА-Я]+\\s[а-я]+\\s\\[\\d+]\$".toRegex()

        val bot = bot {
            token = "5393101475:AAG4uNHFuKBTO_hNKRCcBOHct3QF9EvQWEE"
            dispatch {

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
                    val titleResult = bot.sendMessage(
                            chatId = ChatId.fromId(callbackQuery.message!!.chat.id),
                            text = CommandMessage.ADD_TITLE.message,
                            replyToMessageId = callbackQuery.message!!.messageId,
                            replyMarkup = ForceReplyMarkup(true, selective = true)
                    )

                    titleResult.fold({
                        val result = it!!.result!!
                        observer.add(
                                messageId = result.messageId,
                                chatId = result.chat.id,
                                userId = result.replyToMessage!!.chat.id
                        )
                    })
                }

                callbackQuery("create_task") {

                }

                message(Filter.Reply) {
                    if (message.replyToMessage?.text == CommandMessage.ADD_TITLE.message) {
                        observer.get(message.replyToMessage!!.messageId)?.also { cacheMessage ->
                            if (cacheMessage.chatId == message.chat.id && cacheMessage.userId == message.from?.id) {
                                cacheMessage.createdTime = System.currentTimeMillis()
                                cacheMessage.task.title = message.text
                                observer.remove(message.replyToMessage!!.messageId)

                                val titleResult = bot.sendMessage(
                                        chatId = ChatId.fromId(message.chat.id),
                                        text = CommandMessage.ADD_DESCRIPTION.message,
                                        replyToMessageId = message.messageId,
                                        replyMarkup = ForceReplyMarkup(true, selective = true)
                                )
                                titleResult.fold({
                                    observer.add(it!!.result!!.messageId, cacheMessage)
                                })
                            }
                        }
                    }

                    if (message.replyToMessage?.text == CommandMessage.ADD_DESCRIPTION.message) {
                        observer.get(message.replyToMessage!!.messageId)?.also { cacheMessage ->
                            if (cacheMessage.chatId == message.chat.id && cacheMessage.userId == message.from?.id) {
                                cacheMessage.createdTime = System.currentTimeMillis()
                                cacheMessage.task.description = message.text

                                val task = createTaskCommand.execute(message, cacheMessage.task)
                                observer.remove(message.replyToMessage!!.messageId)

                                bot.sendMessage(
                                        chatId = ChatId.fromId(message.chat.id),
                                        text = "Задача с номером ${task.id} успешно создана!",
                                        replyToMessageId = message.messageId
                                )
                            }
                        }
                    }
                }
            }
        }
        bot.startPolling()

    }


    private enum class CommandMessage(val message: String) {
        ADD_TITLE("Задай название для задачи"),
        ADD_DESCRIPTION("Отлично! А теперь задай описание"),
    }

}


fun main(args: Array<String>) {
    runApplication<ChallengerbotApplication>(*args)

    ChallengerbotApplication().start()
}
