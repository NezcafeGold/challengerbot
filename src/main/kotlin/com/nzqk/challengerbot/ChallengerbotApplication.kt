package com.nzqk.challengerbot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.network.fold
import com.nzqk.challengerbot.command.CreateTaskCommand
import com.nzqk.challengerbot.command.GetMyTasksCommand
import com.nzqk.challengerbot.command.GetTaskCommand
import com.nzqk.challengerbot.observer.CacheMessage
import com.nzqk.challengerbot.observer.MessageObserver
import com.nzqk.challengerbot.utils.ApplicationContextUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class ChallengerbotApplication {

    val appCtx: ApplicationContext by lazy { ApplicationContextUtils.applicationContext!! }
    val createTaskCommand by lazy { appCtx.getBean("createTaskCommand", CreateTaskCommand::class.java) }
    val getMyTasksCommand by lazy { appCtx.getBean("getMyTasksCommand", GetMyTasksCommand::class.java) }
    val getTaskCommand by lazy { appCtx.getBean("getTaskCommand", GetTaskCommand::class.java) }
    val observer = MessageObserver()

    fun start() {

        observer.startTimer()

        val bot = bot {
            token = "5393101475:AAG4uNHFuKBTO_hNKRCcBOHct3QF9EvQWEE"
            dispatch {

                command("help") {
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = " Возможные команды",
                        replyToMessageId = update.message!!.messageId,
                        replyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(
                            listOf(
                                listOf(CommandMessage.CREATE_TASK.message),
                                listOf(CommandMessage.MY_TASK.message),
                                listOf(CommandMessage.GET_TASK.message)
                            )
                        )
                    )
                }

                message {
                    if (message.text == CommandMessage.MY_TASK.message) {
                        val tasks = getMyTasksCommand.execute(message).let {
                            it.joinToString("\n") { task ->
                                task.toShortString()
                            }
                        }
                        bot.sendMyMessage(message, tasks)
                    }

                    if (message.text == CommandMessage.GET_TASK.message) {
                        bot.sendMyMessageWithObserver(message, CommandMessage.GET_TASK_NUM.message)
                    }

                    if (message.text == CommandMessage.CREATE_TASK.message) {
                        bot.sendMyMessageWithObserver(message, CommandMessage.ADD_TITLE.message)
                    }
                }

                message(Filter.Reply) {

                    if (message.replyToMessage?.text == CommandMessage.GET_TASK_NUM.message) {
                        message.findCacheMessage()?.also { cacheMessage ->
                            if (message.checkCorrectUser(cacheMessage)) {
                                getTaskCommand.execute(message).also {
                                    observer.remove(message.replyToMessage!!.messageId)
                                    bot.sendMyMessage(message, it.toString(), null)
                                }
                            }
                        }
                    }

                    if (message.replyToMessage?.text == CommandMessage.ADD_TITLE.message) {
                        message.findCacheMessage()?.also { cacheMessage ->
                            if (message.checkCorrectUser(cacheMessage)) {
                                cacheMessage.createdTime = System.currentTimeMillis()
                                cacheMessage.task.title = message.text
                                cacheMessage.task.chat = message.chat.id
                                observer.remove(message.replyToMessage!!.messageId)

                                bot.sendMyMessageWithObserver(message, CommandMessage.ADD_DESCRIPTION.message, cacheMessage)
                            }
                        }
                    }

                    if (message.replyToMessage?.text == CommandMessage.ADD_DESCRIPTION.message) {
                        message.findCacheMessage()?.also { cacheMessage ->
                            if (message.checkCorrectUser(cacheMessage)) {
                                cacheMessage.createdTime = System.currentTimeMillis()
                                cacheMessage.task.description = message.text

                                observer.remove(message.replyToMessage!!.messageId)

                                bot.sendMyMessageWithObserver(message, CommandMessage.ADD_DEADLINE.message, cacheMessage)
                            }
                        }
                    }

                    if (message.replyToMessage?.text == CommandMessage.ADD_DEADLINE.message ||
                        message.replyToMessage?.text == CommandMessage.ADD_DEADLINE_WRONG.message
                    ) {
                        message.findCacheMessage()?.also { cacheMessage ->
                            if (message.checkCorrectUser(cacheMessage)) {
                                cacheMessage.createdTime = System.currentTimeMillis()

                                val days = message.text!!.trim().toIntOrNull()

                                if (days == null) {
                                    observer.remove(message.replyToMessage!!.messageId)
                                    bot.sendMyMessageWithObserver(message, CommandMessage.ADD_DEADLINE_WRONG.message, cacheMessage)
                                } else {
                                    cacheMessage.task.deadlinePlusHours(days.toLong())
                                    val task = createTaskCommand.execute(message, cacheMessage.task)
                                    observer.remove(message.replyToMessage!!.messageId)

                                    bot.sendMyMessage(message, "Задача с номером ${task.id} успешно создана!", null)
                                }
                            }
                        }
                    }
                }
            }
        }
        bot.startPolling()
    }

    private fun Message.findCacheMessage() = observer.get(replyToMessage!!.messageId)

    private fun Message.checkCorrectUser(cacheMessage: CacheMessage) = cacheMessage.chatId == chat.id && cacheMessage.userId == from?.id

    private fun sendMessage(message: Message, bot: Bot, text: String) {
        bot.sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = text,
            replyToMessageId = message.messageId
        )
    }

    private fun Bot.sendMyMessageWithObserver(
        message: Message,
        text: String,
        cacheMessage: CacheMessage? = null
    ) =
        sendMyMessage(message, text).also {
            it.fold({ resp ->
                val result = resp!!.result!!
                if (cacheMessage == null) {
                    observer.add(
                        messageId = result.messageId,
                        chatId = result.chat.id,
                        userId = result.replyToMessage!!.from!!.id,
                    )
                } else {
                    observer.add(
                        messageId = result.messageId,
                        cacheMessage = cacheMessage
                    )
                }
            })
        }

    private fun Bot.sendMyMessage(
        message: Message,
        text: String,
        replyMarkup: ReplyMarkup? = ForceReplyMarkup(true, selective = true)
    ) =
        sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = text,
            parseMode = ParseMode.HTML,
            replyToMessageId = message.messageId,
            replyMarkup = replyMarkup
        )
}

private enum class CommandMessage(val message: String) {
    CREATE_TASK("Создать задачу"),
    MY_TASK("Мои задачи"),
    GET_TASK("Вывести задачу"),
    GET_TASK_NUM("Введи номер задачи"),
    ADD_TITLE("Задай название для задачи."),
    ADD_DESCRIPTION("Отлично! А теперь задай описание."),
    ADD_DEADLINE("За сколько дней выполнишь? (это сейчас часы)"),
    ADD_DEADLINE_WRONG("Не могу разобрать. Отправь только количество дней цифрой."),
}

fun main(args: Array<String>) {
    runApplication<ChallengerbotApplication>(*args)

    ChallengerbotApplication().start()
}
