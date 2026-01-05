package dev.httpmarco.polocloud.module.gradle

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.gson.GsonBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import java.io.File

/**
 * Gradle plugin for building PoloCloud modules.
 *
 * This plugin automatically generates module `module.json` from build.gradle.kts configuration
 * and packages it into the module JAR file.
 * ```
 */
class PolocloudModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<PolocloudModuleExtension>("polocloudModule", project)

        project.plugins.apply("java")
        configureJarTask(project, extension)
        registerBuildModuleTask(project, extension)
        registerShadowBuildModuleTask(project, extension)
    }

    private fun configureJarTask(project: Project, extension: PolocloudModuleExtension) {
        project.tasks.named<Jar>("jar") {
            doFirst {
                validateConfiguration(project, extension)
                val metadata = generateModuleMetadata(project, extension)
                val metadataFile = File(project.buildDir, "tmp/module.json")
                metadataFile.parentFile.mkdirs()
                metadataFile.writeText(metadata)
            }

            from(File(project.buildDir, "tmp/module.json")) {
                into("")
            }
        }
    }

    private fun registerBuildModuleTask(project: Project, extension: PolocloudModuleExtension) {
        project.tasks.register("buildModule") {
            group = "polocloud"
            description = "Builds the PoloCloud module JAR with generated module.json"
            dependsOn("jar")

            doLast {
                val jarTask = project.tasks.getByName<Jar>("jar")
                val outputFile = jarTask.archiveFile.get().asFile

                project.logger.lifecycle("")
                project.logger.lifecycle("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                project.logger.lifecycle("✓ PoloCloud Module built successfully!")
                project.logger.lifecycle("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                project.logger.lifecycle("  Module ID:   ${extension.idProperty.getOrElse("unknown")}")
                project.logger.lifecycle("  Name:        ${extension.moduleNameProperty.getOrElse("unknown")}")
                project.logger.lifecycle("  Version:     ${extension.versionProperty.getOrElse("unknown")}")
                project.logger.lifecycle("  Output:      ${outputFile.absolutePath}")
                project.logger.lifecycle("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                project.logger.lifecycle("")
            }
        }
    }

    private fun registerShadowBuildModuleTask(project: Project, extension: PolocloudModuleExtension) {
        project.tasks.register<ShadowJar>("shadowBuildModule") {
            group = "polocloud"
            description = "Builds a shaded PoloCloud module JAR with all dependencies"
            dependsOn("buildModule")

            archiveFileName.set("polocloud-${extension.idProperty.get()}-${extension.versionProperty.get()}-shaded.jar")

            val jarTask = project.tasks.named<Jar>("jar").get()
            from(jarTask.archiveFile.map { project.zipTree(it) })

            configurations = listOf(project.configurations.getByName("runtimeClasspath"))
            mergeServiceFiles()

            doLast {
                val outputFile = archiveFile.get().asFile

                project.logger.lifecycle("")
                project.logger.lifecycle("┌─────────────────────────────────────────────┐")
                project.logger.lifecycle("│ ✓ Shaded PoloCloud Module built!           │")
                project.logger.lifecycle("└─────────────────────────────────────────────┘")
                project.logger.lifecycle("  Module ID:   ${extension.idProperty.getOrElse("unknown")}")
                project.logger.lifecycle("  Name:        ${extension.moduleNameProperty.getOrElse("unknown")}")
                project.logger.lifecycle("  Version:     ${extension.versionProperty.getOrElse("unknown")}")
                project.logger.lifecycle("  Output:      ${outputFile.absolutePath}")
                project.logger.lifecycle("└─────────────────────────────────────────────┘")
                project.logger.lifecycle("")
            }
        }
    }

    private fun validateConfiguration(project: Project, extension: PolocloudModuleExtension) {
        val errors = mutableListOf<String>()

        if (!extension.idProperty.isPresent) errors.add("Module ID is required")
        if (!extension.versionProperty.isPresent) errors.add("Module version is required")
        if (!extension.moduleNameProperty.isPresent) errors.add("Module name is required")
        if (!extension.mainClassProperty.isPresent) errors.add("Main class is required")
        if (!extension.authorProperty.isPresent) errors.add("Author is required")

        if (errors.isNotEmpty()) {
            throw IllegalStateException(
                "PoloCloud module configuration is incomplete:\n${errors.joinToString("\n") { "  - $it" }}"
            )
        }
    }

    private fun generateModuleMetadata(project: Project, extension: PolocloudModuleExtension): String {
        val metadata = buildMap {
            put("id", extension.idProperty.get())
            put("name", extension.moduleNameProperty.get())
            put("version", extension.versionProperty.get())
            put("description", extension.descriptionProperty.getOrElse(""))
            put("author", extension.authorProperty.get())
            put("main", extension.mainClassProperty.get())

            if (extension.loadOrderProperty.isPresent) {
                put("loadOrder", extension.loadOrderProperty.get().name)
            }

            if (extension.apiVersionProperty.isPresent) {
                put("apiVersion", extension.apiVersionProperty.get())
            }
        }

        return GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(metadata)
    }

}