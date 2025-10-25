package org.teamvoided.reef.init

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component.literal
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult


object ReefDebug {
    fun init() {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment) return

        /*   UseBlockCallback.EVENT.register() { player, world, hand, hitResult ->
               if (useDebug(player, world, hand, hitResult)) {
                   InteractionResult.SUCCESS
               } else InteractionResult.PASS
           }*/
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

    @Suppress("unused")
    fun useDebug(
        player: Player,
        world: Level,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): Boolean {
        if (world.isClientSide) return true
        if (player.isCrouching) return false

        return false
    }

    fun Player.info(str: String) = displayClientMessage(literal(str), false)
}
