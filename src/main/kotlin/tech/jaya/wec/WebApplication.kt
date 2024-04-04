package tech.jaya.wec

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WecApplication

fun main(args: Array<String>) {
    runApplication<WecApplication>(*args)
}