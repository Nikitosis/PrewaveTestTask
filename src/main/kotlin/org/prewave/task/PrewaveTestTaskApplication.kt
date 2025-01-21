package org.prewave.task

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PrewaveTestTaskApplication

fun main(args: Array<String>) {
    runApplication<PrewaveTestTaskApplication>(*args)
}
