package com.nzqk.challengerbot.command

import com.github.kotlintelegrambot.entities.Message

interface ICommand<T : Any> {
    fun execute(message: Message): T
}