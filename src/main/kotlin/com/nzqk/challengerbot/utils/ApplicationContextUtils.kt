package com.nzqk.challengerbot.utils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component


@Component
class ApplicationContextUtils : ApplicationContextAware {

    override fun setApplicationContext(appContext: ApplicationContext) {
        ctx = appContext
    }

    companion object {
        private var ctx: ApplicationContext? = null
        val applicationContext: ApplicationContext?
            get() = ctx
    }
}