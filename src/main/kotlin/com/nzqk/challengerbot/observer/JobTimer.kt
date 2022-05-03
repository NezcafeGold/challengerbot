package com.nzqk.challengerbot.observer

import kotlin.concurrent.thread

abstract class JobTimer(private val timeForCheck: Long) {

    private var thread: Thread

    init {
        thread = thread {
            var savedTime = System.currentTimeMillis()
            while (true) {
                if (System.currentTimeMillis() - savedTime > timeForCheck) {
                    job()
                    savedTime = System.currentTimeMillis()
                }
            }
        }
    }

    abstract fun job()
}
