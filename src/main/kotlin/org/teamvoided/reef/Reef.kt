package org.teamvoided.reef

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.ResourceLocation
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


        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            /*
            CustomSurfaceBuilder.PRE_VANILLA.register { defaulState, seaLevel, biomes, chunk, blockColumn, x, z ->
                val y = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, x, z) + 1

                val newX = sin(x / 5.0) * 5
                val newZ = sin(z / 5.0) * 5
                if ((newX + newZ) > 0) {
                    repeat((newX + newZ).toInt()) { blockColumn.setState(y + it, defaulState) }
                }

            }
            CustomSurfaceBuilder.POST_VANILLA.register { defaulState, seaLevel, biomes, chunk, blockColumn, x, z ->
                 val y = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, x, z) + 1
                 if (x % 4 == 0 && z % 4 == 0) {
                     repeat(3) { blockColumn.setState(y + it, Blocks.YELLOW_STAINED_GLASS.defaultState) }
                 }
             }*/
        }
    }

//          log.info("This is marine invertebrate takeover!")

    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MODID, path)
}
