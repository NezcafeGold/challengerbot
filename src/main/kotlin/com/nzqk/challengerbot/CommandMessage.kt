package com.nzqk.challengerbot

enum class CommandMessage(val message: String) {
    CREATE_TASK("Создать задачу"),
    MY_TASK("Мои задачи"),
    GET_TASK("Вывести задачу"),
    GET_TASK_NUM("Введи номер задачи"),
    ADD_TITLE("Задай название для задачи."),
    ADD_DESCRIPTION("Отлично! А теперь задай описание."),
    ADD_DEADLINE("За сколько дней выполнишь? (это сейчас часы)"),
    ADD_DEADLINE_WRONG("Не могу разобрать. Отправь только количество дней цифрой."),
}
