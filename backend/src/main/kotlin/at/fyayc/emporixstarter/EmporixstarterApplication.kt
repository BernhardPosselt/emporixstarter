package at.fyayc.emporixstarter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(Properties::class)
@SpringBootApplication
class EmporixstarterApplication

fun main(args: Array<String>) {
    runApplication<EmporixstarterApplication>(*args)
}
