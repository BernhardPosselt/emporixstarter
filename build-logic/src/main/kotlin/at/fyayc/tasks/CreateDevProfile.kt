package at.fyayc.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class CreateDevProfile : DefaultTask() {
    @get:OutputFile
    abstract val file: RegularFileProperty

    @TaskAction
    fun run() {
        val configYaml = """
        emporixstarter:
          users:
            actuator:
              password: "developmentpassword"
        """.trimIndent()
        file.get().asFile.writeText(configYaml)
    }
}
