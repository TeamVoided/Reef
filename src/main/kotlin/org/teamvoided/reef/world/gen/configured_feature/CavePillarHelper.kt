package org.teamvoided.reef.world.gen.configured_feature

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.WorldAccess
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow

@Suppress("unused")
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

    fun canGenerateBase(world: StructureWorldAccess, pos: BlockPos, height: Int): Boolean {
        if (canGenerateOrLava(world, pos)) return false

        val g = 6.0f / height.toFloat()
        var h = 0.0f

        while (h < Math.PI * 2) {
            val i = (MathHelper.cos(h) * height.toFloat()).toInt()
            if (!canGenerateOrLava(world, pos.add(i, 0, (MathHelper.sin(h) * height.toFloat()).toInt()))) {
                h += g
                continue
            }
            return false
        }
        return true
    }

    fun canGenerate(world: WorldAccess, pos: BlockPos?): Boolean {
        return world.testBlockState(pos, CavePillarHelper::canGenerate)
    }

    fun canGenerateOrLava(world: WorldAccess, pos: BlockPos): Boolean {
        return world.testBlockState(pos, CavePillarHelper::canGenerateOrLava)
    }

//    fun getCavePillarThickness(direction: Direction, height: Int, merge: Boolean, callback: Consumer<BlockState>) {
//        if (height >= 3) {
//            callback.accept(getState(direction, Thickness.BASE))
//            for (i in 0 until height - 3) {
//                callback.accept(getState(direction, Thickness.MIDDLE))
//            }
//        }
//        if (height >= 2) {
//            callback.accept(getState(direction, Thickness.FRUSTUM))
//        }
//        if (height >= 1) {
//            callback.accept(getState(direction, if (merge) Thickness.TIP_MERGE else Thickness.TIP))
//        }
//    }

//    fun generatePointedCavePillar(world: WorldAccess, pos: BlockPos, direction: Direction, height: Int, merge: Boolean) {
//        if (!canReplace(world.getBlockState(pos.offset(direction.opposite)))) {
//            return
//        }
//        val mutable = pos.mutableCopy()
//        getCavePillarThickness(direction, height, merge) { state: BlockState ->
//            var state = state
//            if (state.isOf(Blocks.POINTED_CavePillar)) {
//                state = state.with(PointedCavePillarBlock.WATERLOGGED, world.isWater(mutable)) as BlockState
//            }
//            world.setBlockState(mutable, state, Block.NOTIFY_LISTENERS)
//            mutable.move(direction)
//        }
//    }
//
//    fun generateCavePillarBlock(world: WorldAccess, pos: BlockPos?): Boolean {
//        val blockState = world.getBlockState(pos)
//        if (blockState.isIn(BlockTags.CavePillar_REPLACEABLE)) {
//            world.setBlockState(pos, Blocks.CavePillar_BLOCK.defaultState, Block.NOTIFY_LISTENERS)
//            return true
//        }dded LargeCavePillarFeature
//        return false
//    }
//
//    private fun getState(direction: Direction, thickness: Thickness): BlockState {
//        return (Blocks.POINTED_CavePillar.defaultState.with(
//            PointedCavePillarBlock.VERTICAL_DIRECTION,
//            direction
//        )).with(PointedCavePillarBlock.THICKNESS, thickness)
//    }

    fun canReplaceOrLava(state: BlockState): Boolean {
        return canReplace(state) || state.isOf(Blocks.LAVA)
    }

    private fun canReplace(@Suppress("UNUSED_PARAMETER") state: BlockState): Boolean {
        return true
//        state.isIn(DuskBlockTags.CAVE_PILLAR_REPLACEABLE)
    }

    fun canGenerate(state: BlockState): Boolean {
        return state.isAir || state.isOf(Blocks.WATER)
    }

    fun isNeitherEmptyNorWater(state: BlockState): Boolean {
        return !state.isAir && !state.isOf(Blocks.WATER)
    }

    fun canGenerateOrLava(state: BlockState): Boolean {
        return state.isAir || state.isOf(Blocks.WATER) || state.isOf(Blocks.LAVA)
    }
}

