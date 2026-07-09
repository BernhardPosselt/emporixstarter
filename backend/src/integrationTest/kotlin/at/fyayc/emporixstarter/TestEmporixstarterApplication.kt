package at.fyayc.emporixstarter

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<EmporixstarterApplication>().with(TestcontainersConfiguration::class).run(*args)
}
