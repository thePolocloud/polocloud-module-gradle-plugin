package dev.httpmarco.polocloud.module.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Extension for configuring PoloCloud module metadata in build.gradle.kts.
 *
 * Example usage:
 * ```kotlin
 * polocloudModule {
 *     id = "my-module"
 *     moduleName = "My Module"
 *     description = "A module that does cool things"
 *     author = "YourName"
 *     mainClass = "com.example.MyModule"
 *
 *     // Optional
 *     loadOrder = LoadOrder.STARTUP
 *     apiVersion = ">=3.0.0-pre.7-SNAPSHOT"
 *
 * }
 * ```
 */
abstract class PolocloudModuleExtension(private val project: Project) {

    /**
     * Unique identifier for the module (e.g., "example-module").
     * This should be lowercase with hyphens.
     */
    @get:Input
    abstract val idProperty: Property<String>

    /**
     * Unique identifier for the module (e.g., "example-module").
     * This should be lowercase with hyphens.
     */
    @get:Input
    abstract val versionProperty: Property<String>

    /**
     * Human-readable name of the module (e.g., "Example Module").
     */
    @get:Input
    abstract val moduleNameProperty: Property<String>

    /**
     * Description of what the module does.
     */
    @get:Input
    @get:Optional
    abstract val descriptionProperty: Property<String>

    /**
     * Author of the module.
     */
    @get:Input
    abstract val authorProperty: Property<String>

    /**
     * Fully qualified main class that implements PolocloudModule.
     */
    @get:Input
    abstract val mainClassProperty: Property<String>

    /**
     * Load order of the module.
     * Valid values: STARTUP, POST_STARTUP, LATE
     * Default: STARTUP
     */
    @get:Input
    @get:Optional
    abstract val loadOrderProperty: Property<LoadOrder>

    /**
     * API version this module was built for.
     * Default: >=3.0.0-pre.7-SNAPSHOT
     */
    @get:Input
    @get:Optional
    abstract val apiVersionProperty: Property<String>

    var id: String
        get() = idProperty.get()
        set(value) = idProperty.set(value)

    var version: String
        get() = versionProperty.get()
        set(value) = versionProperty.set(value)

    var moduleName: String
        get() = moduleNameProperty.get()
        set(value) = moduleNameProperty.set(value)

    var description: String
        get() = descriptionProperty.get()
        set(value) = descriptionProperty.set(value)

    var author: String
        get() = authorProperty.get()
        set(value) = authorProperty.set(value)

    var mainClass: String
        get() = mainClassProperty.get()
        set(value) = mainClassProperty.set(value)

    var loadOrder: LoadOrder
        get() = loadOrderProperty.get()
        set(value) = loadOrderProperty.set(value)

    var apiVersion: String
        get() = apiVersionProperty.get()
        set(value) = apiVersionProperty.set(value)

    init {
        descriptionProperty.convention("")
        loadOrderProperty.convention(LoadOrder.STARTUP)
        apiVersionProperty.convention(">=3.0.0-pre.8-SNAPSHOT")
    }
}