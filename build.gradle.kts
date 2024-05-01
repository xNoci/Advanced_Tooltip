plugins {
    id("java-library")
    id("net.labymod.gradle")
    id("net.labymod.gradle.addon")
}

group = "me.noci"
version = "1.6.0"

labyMod {
    defaultPackageName = "me.noci.advancedtooltip"
    addonInfo {
        namespace = "advancedtooltip"
        displayName = "Advanced Tooltip"
        author = "Noci"
        description = "Add more information to the tooltip of an item."
        minecraftVersion = "*"
        version = getVersion() as String
    }

    minecraft {
        registerVersions(
            "1.8.9",
            "1.12.2",
            "1.16.5",
            "1.17.1",
            "1.18.2",
            "1.19.2", "1.19.3", "1.19.4",
            "1.20.1", "1.20.2", "1.20.4", "1.20.5", "1.20.6"
        ) { version, provider ->
            configureRun(provider, version)
        }

        subprojects.forEach {
            if (it.name != "game-runner") {
                filter(it.name)
            }
        }
    }

    addonDev {
        productionRelease()
    }
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("net.labymod.gradle")
    plugins.apply("net.labymod.gradle.addon")

    repositories {
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
}

fun configureRun(provider: net.labymod.gradle.core.minecraft.provider.VersionProvider, gameVersion: String) {
    provider.runConfiguration {
        mainClass = "net.minecraft.launchwrapper.Launch"
        jvmArgs("-Dnet.labymod.running-version=${gameVersion}")
        jvmArgs("-Dmixin.debug=true")
        jvmArgs("-Dnet.labymod.debugging.all=true")
        jvmArgs("-Dmixin.env.disableRefMap=true")

        args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
        args("--labymod-dev-environment", "true")
        args("--addon-dev-environment", "true")
    }

    provider.javaVersion = JavaVersion.VERSION_21

    provider.mixin {
        val mixinMinVersion = when (gameVersion) {
            "1.8.9", "1.12.2", "1.16.5" -> {
                "0.6.6"
            }

            else -> {
                "0.8.2"
            }
        }

        minVersion = mixinMinVersion
    }
}
