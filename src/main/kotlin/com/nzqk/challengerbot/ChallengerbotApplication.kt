package com.nzqk.challengerbot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ChallengerbotApplication

@Value("\${bot.api.key}")
val token: String = ""

fun main(args: Array<String>) {
    runApplication<ChallengerbotApplication>(*args)
    val bot = bot {
        token = token
        dispatch {
            text {
                bot.sendMessage(ChatId.fromId(message.chat.id), text = text)
            }
        }
    }
    bot.startPolling()
}
