package org.teamvoided.reef

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.reef.init.ReefFeatures

@Suppress("unused")
object Reef {
    private const val MODID = "reef"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Reef::class.simpleName)

    fun commonInit() {
        log.info("Adding coral to your Worlds!")
        ReefFeatures.init()
    }

  /*  fun clientInit() {
        log.info("This is marine invertebrate takeover!")
    }*/

    fun id(path: String) = Identifier.of(MODID, path)
}
