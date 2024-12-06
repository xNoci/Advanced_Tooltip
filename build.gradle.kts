plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "me.noci"
version = providers.environmentVariable("VERSION").getOrElse("1.8.5")

labyMod {
    defaultPackageName = "me.noci.advancedtooltip"

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                    //TODO
                }
            }
        }
    }

    addonInfo {
        namespace = "advancedtooltip"
        displayName = "Advanced Tooltip"
        author = "Noci"
        description = "Add more information to the tooltip of an item."
        minecraftVersion = "*"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}