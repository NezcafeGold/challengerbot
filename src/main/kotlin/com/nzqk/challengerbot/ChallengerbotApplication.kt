package com.nzqk.challengerbot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ChallengerbotApplication

fun main(args: Array<String>) {
	runApplication<ChallengerbotApplication>(*args)

	val bot = bot {
		token = "YOUR_API_KEY"
		dispatch {
			text {
				bot.sendMessage(ChatId.fromId(message.chat.id), text = text)
			}
		}
	}
	bot.startPolling()
}
