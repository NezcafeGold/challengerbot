package com.nzqk.challengerbot.model

enum class TaskStatus(val message: String) {
    PROGRESS("В прогрессе"),
    NOTIFIED("Необходимо установить статус"),
    CLOSED("Задача закрыта"),
    DONE("Задача успешно выполнена!"),
    FAILED("Задача провалена...")
}
