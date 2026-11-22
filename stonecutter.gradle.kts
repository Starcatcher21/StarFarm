plugins {
    id("dev.kikugie.stonecutter")
    id("gg.meza.stonecraft")
}

tasks.register("publishAll") {
    group = "publishing"
    dependsOn(stonecutter.versions.map { ":${it.project}:publishMod" })
}

stonecutter active "26.3-fabric" /* [SC] DO NOT EDIT */
