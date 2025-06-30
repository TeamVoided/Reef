package org.teamvoided.reef.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.util.Mth
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import org.teamvoided.reef.world.gen.configured_feature.config.SpikeFeatureConfig
import kotlin.math.abs

class InvertedSpikeFeature(configCodec: Codec<SpikeFeatureConfig>?) :
    Feature<SpikeFeatureConfig>(configCodec) {

    override fun place(context: FeaturePlaceContext<SpikeFeatureConfig>): Boolean {
        val config = context.config()

        var blockPos = context.origin()
        val random = context.random()
        val world = context.level()


        if (!world.getBlockState(blockPos).`is`(config.canReplace)) return false
        blockPos = blockPos.below(random.nextInt(4))
        val i = random.nextInt(4) + 7
        val j = i / 4 + random.nextInt(2)
        if (j > 1 && random.nextInt(config.chanceForLongSpike) == 0) {
            blockPos = blockPos.below(random.nextInt(config.longSpikeOffsetMin, config.longSpikeOffsetMax))
        }

        var k = 0
        var l: Int
        while (k < i) {
            val f = (1f - k.toFloat() / i.toFloat()) * j.toFloat()
            l = Mth.ceil(f)
            for (m in -l..l) {
                val g = Mth.abs(m) - 0.25f
                for (n in -l..l) {
                    val h = Mth.abs(n) - 0.25f
                    if ((m == 0 && n == 0 || !(g * g + h * h > f * f)) && (m != -l && m != l && n != -l && n != l || !(random.nextFloat() > 0.75f))) {
                        var blockState = world.getBlockState(blockPos.offset(m, -k, n))
                        if (blockState.`is`(config.canReplace)) {
                            this.setBlock(
                                world,
                                blockPos.offset(m, -k, n),
                                config.baseBlock.getState(random, blockPos.offset(m, -k, n))
                            )
                        }

                        if (k != 0 && l > 1) {
                            blockState = world.getBlockState(blockPos.offset(m, k, n))
                            if (blockState.`is`(config.canReplace)) {
                                this.setBlock(
                                    world,
                                    blockPos.offset(m, k, n),
                                    config.baseBlock.getState(random, blockPos.offset(m, k, n))
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
                var blockPos2 = context.origin().offset(o, 2, q)
                var p = if (abs(o) == 1 && abs(q) == 1) random.nextInt(5) else 50

                while (blockPos2.y > blockPos.y) {
                    this.setBlock(world, blockPos2, config.baseBlock.getState(random, blockPos2))
                    blockPos2 = blockPos2.below()
                    if (--p > 0) continue
                    blockPos2 = blockPos2.below(random.nextInt(5) + 1)
                    p = random.nextInt(5)
                }
            }
        }

        return true
    }
}