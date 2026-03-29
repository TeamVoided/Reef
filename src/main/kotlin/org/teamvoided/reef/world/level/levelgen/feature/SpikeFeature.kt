package org.teamvoided.reef.world.level.levelgen.feature

import com.mojang.serialization.Codec
import net.minecraft.util.Mth
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import org.teamvoided.reef.world.level.levelgen.feature.config.SpikeFeatureConfig
import kotlin.math.abs

class SpikeFeature(configCodec: Codec<SpikeFeatureConfig>) : Feature<SpikeFeatureConfig>(configCodec) {

    override fun place(context: FeaturePlaceContext<SpikeFeatureConfig>): Boolean {
        val config = context.config()
        var blockPos = context.origin()
        val random = context.random()
        val world = context.level()
        while (world.isEmptyBlock(blockPos) && blockPos.y > world.minY + 2) {
            blockPos = blockPos.below()
        }

        if (!world.getBlockState(blockPos).`is`(config.canReplace)) {
            return false
        } else {
            blockPos = blockPos.above(random.nextInt(4))
            val i = random.nextInt(4) + 7
            val j = i / 4 + random.nextInt(2)
            if (j > 1 && random.nextInt(config.chanceForLongSpike) == 0) {
                blockPos = blockPos.below(random.nextInt(config.longSpikeOffsetMin, config.longSpikeOffsetMax))
            }

            var k: Int
            var l: Int
            k = 0
            while (k < i) {
                val f = (1.0f - k.toFloat() / i.toFloat()) * j.toFloat()
                l = Mth.ceil(f)

                for (m in -l..l) {
                    val g = Mth.abs(m).toFloat() - 0.25f

                    for (n in -l..l) {
                        val h = Mth.abs(n).toFloat() - 0.25f
                        if ((m == 0 && n == 0 || !(g * g + h * h > f * f)) && (m != -l && m != l && n != -l && n != l || !(random.nextFloat() > 0.75f))) {
                            var blockState = world.getBlockState(blockPos.offset(m, k, n))
                            if (blockState.`is`(config.canReplace)) {
                                this.setBlock(
                                    world,
                                    blockPos.offset(m, k, n),
                                    config.baseBlock.getState(random, blockPos.offset(m, k, n))
                                )
                            }

                            if (k != 0 && l > 1) {
                                blockState = world.getBlockState(blockPos.offset(m, -k, n))
                                if (blockState.`is`(config.canReplace)) {
                                    this.setBlock(
                                        world,
                                        blockPos.offset(m, -k, n),
                                        config.baseBlock.getState(random, blockPos.offset(m, -k, n))
                                    )
                                }
                            }
                        }
                    }
                }
                ++k
            }

            k = j - 1
            if (k < 0) {
                k = 0
            } else if (k > 1) {
                k = 1
            }

            for (o in -k..k) {
                for (q in -k..k) {
                    var blockPos2 = blockPos.offset(o, -1, q)
                    var p = 50
                    if (abs(o) == 1 && abs(q) == 1) {
                        p = random.nextInt(5)
                    }

                    while (blockPos2.y > 50) {
                        val blockState2 = world.getBlockState(blockPos2)
                        if (!blockState2.`is`(config.canReplace)
                            && !blockState2.`is`(config.baseBlock.getState(random, blockPos2).block)
                        ) break


                        this.setBlock(world, blockPos2, config.baseBlock.getState(random, blockPos2))
                        blockPos2 = blockPos2.below()
                        --p
                        if (p <= 0) {
                            blockPos2 = blockPos2.below(random.nextInt(5) + 1)
                            p = random.nextInt(5)
                        }
                    }
                }
            }

            return true
        }
    }

}