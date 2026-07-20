import org.gradle.api.tasks.bundling.AbstractArchiveTask
import java.util.Properties

plugins { id("net.neoforged.moddev.legacyforge") }

val versionProperties = Properties().apply {
    file("gradle.properties").inputStream().use(::load)
}
fun prop(name: String): String = versionProperties.getProperty(name)
    ?: rootProject.findProperty(name)?.toString()
    ?: error("Missing property '$name'")

version = prop("mod_version")
base.archivesName = prop("archives_base_name")

repositories { maven("https://api.modrinth.com/maven") }

legacyForge {
    version = prop("deps.forge")
    mods { register("rosetta_library") { sourceSet(sourceSets.main.get()) } }
}

dependencies {
    modImplementation("maven.modrinth:data-anchor:${prop("deps.data-anchor")}")
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "minecraft_version_range" to prop("deps.minecraft_range"),
        "loader_version_range" to prop("deps.forge_range"),
        "dataanchor_version" to prop("deps.data-anchor").substringBefore('-'),
    )
    inputs.properties(props)
    filesMatching("META-INF/mods.toml") { expand(props) }
    exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
}

val targetJavaVersion = prop("java_version").toInt()
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}
tasks.named<AbstractArchiveTask>("sourcesJar") { archiveClassifier.set("${project.name}-sources") }
tasks.jar { archiveClassifier.set(project.name) }
