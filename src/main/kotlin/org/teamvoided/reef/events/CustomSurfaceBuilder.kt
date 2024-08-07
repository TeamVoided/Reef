package org.teamvoided.reef.events

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.world.biome.source.BiomeAccess
import net.minecraft.world.gen.chunk.BlockColumn
import org.teamvoided.reef.events.CustomSurfaceBuilder.MakeSurfaceBuilderCallback

object CustomSurfaceBuilder {
    @JvmField
    val PRE_VANILLA = EventFactory.createArrayBacked(MakeSurfaceBuilderCallback::class.java) { listeners ->
        MakeSurfaceBuilderCallback { blockState, seaLvl, blockColumn, biomes, x, z ->
            listeners.forEach { it.register(blockState, seaLvl, blockColumn, biomes, x, z) }
        }
    }

    @JvmField
    val POST_VANILLA = EventFactory.createArrayBacked(MakeSurfaceBuilderCallback::class.java) { listeners ->
        MakeSurfaceBuilderCallback { blockState, seaLvl, blockColumn, biomes, x, z ->
            listeners.forEach { it.register(blockState, seaLvl, blockColumn, biomes, x, z) }
        }
    }

    fun interface MakeSurfaceBuilderCallback {
        @Suppress("LongParameterList")
        fun register(
            defaultBlock: BlockState, seaLevel: Int,
            chunkBlockColumn: BlockColumn, biomeAccess: BiomeAccess, x: Int, z: Int
        )
    }
}
