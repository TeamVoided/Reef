package org.teamvoided.reef

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object Reef {
    const val MODID = "reef"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(Reef::class.simpleName)

    fun commonInit() {
        log.info("Hello from Common")
    }

    fun clientInit() {
        log.info("Hello from Client")
    }

    fun id(path: String) = Identifier(MODID, path)
}
