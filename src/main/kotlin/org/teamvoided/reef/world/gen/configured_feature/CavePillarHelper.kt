package org.teamvoided.reef.world.gen.configured_feature

import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow

object CavePillarHelper {
    fun scaleHeightFromRadius(radius: Double, scale: Double, heightScale: Double, bluntness: Double): Double {
        val r = if (radius < bluntness) bluntness else radius

        val e = r / scale * 0.384
        val f: Double = 0.75 * e.pow(1.3333333333333333)
        val g: Double = e.pow(0.6666666666666666)
        val h = 0.3333333333333333 * ln(e)
        val i = max((heightScale * (f - g - h)), 0.0)

        return i / 0.384 * scale
    }

    fun canGenerateBase(world: WorldGenLevel, pos: BlockPos, height: Int): Boolean {
        if (canGenerateOrLava(world, pos)) return false

        val g = 6.0f / height.toFloat()
        var h = 0.0f

        while (h < Math.PI * 2) {
            val i = (Mth.cos(h) * height.toFloat()).toInt()
            if (!canGenerateOrLava(world, pos.offset(i, 0, (Mth.sin(h) * height.toFloat()).toInt()))) {
                h += g
                continue
            }
            return false
        }
        return true
    }

    fun canGenerate(world: LevelAccessor, pos: BlockPos): Boolean {
        return world.isStateAtPosition(pos, CavePillarHelper::canGenerate)
    }

    fun canGenerateOrLava(world: LevelAccessor, pos: BlockPos): Boolean {
        return world.isStateAtPosition(pos, CavePillarHelper::canGenerateOrLava)
    }

    fun canGenerate(state: BlockState): Boolean {
        return state.isAir || state.`is`(Blocks.WATER)
    }

    fun canGenerateOrLava(state: BlockState): Boolean {
        return state.isAir || state.`is`(Blocks.WATER) || state.`is`(Blocks.LAVA)
    }
}

