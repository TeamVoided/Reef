package org.teamvoided.reef

import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.reef.init.ReefDebug
import org.teamvoided.reef.init.ReefFeatures
import org.teamvoided.reef.init.ReefStructurePoolElementType
import org.teamvoided.reef.init.ReefStructureProcessorType

@Suppress("unused")
object Reef {
    private const val MODID = "reef"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Reef::class.simpleName)

    fun commonInit() {
        log.info("Adding coral to your Worlds!")
        ReefFeatures.init()
        ReefStructurePoolElementType.init()
        ReefStructureProcessorType.init()
        ReefDebug.init()
    }

    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MODID, path)
}
