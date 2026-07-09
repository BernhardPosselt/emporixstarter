package at.fyayc.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths

abstract class MyTask: DefaultTask() {
    @get:Input
    abstract val value: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun exec() {
        // providers lambdas are executed using .get()
        Files.writeString(
            Paths.get(outputFile.get().asFile.path),
            value.get()
        )
    }
}