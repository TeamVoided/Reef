package org.teamvoided.reef.api.events

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.BlockColumn
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.levelgen.RandomState

object CustomSurfaceBuilder {
    @JvmField
    val PRE_RULES: Event<MakeSurfaceBuilderCallback> = EventFactory.createArrayBacked(MakeSurfaceBuilderCallback::class.java) { listeners ->
        MakeSurfaceBuilderCallback { randomState, blockState, seaLvl, biomes, chunk, blockColumn, x, z ->
            listeners.forEach { it.register(randomState, blockState, seaLvl, biomes, chunk, blockColumn, x, z) }
        }
    }

    @JvmField
    val POST_RULES: Event<MakeSurfaceBuilderCallback> = EventFactory.createArrayBacked(MakeSurfaceBuilderCallback::class.java) { listeners ->
        MakeSurfaceBuilderCallback { randomState, blockState, seaLvl, biomes, chunk, blockColumn, x, z ->
            listeners.forEach { it.register(randomState, blockState, seaLvl, biomes, chunk, blockColumn, x, z) }
        }
    }

    fun interface MakeSurfaceBuilderCallback {
        fun register(
            randomState: RandomState,
            defaultBlock: BlockState, seaLevel: Int,
            biomeAccess: BiomeManager, chunk: ChunkAccess,
            chunkBlockColumn: BlockColumn, x: Int, z: Int,
        )
    }
}
