package org.teamvoided.reef.world.level.levelgen.feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.Column
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.phys.Vec3
import org.teamvoided.reef.world.level.levelgen.feature.config.LargeCavePillarFeatureConfig
import kotlin.math.min

class LargeCavePillarFeature(codec: Codec<LargeCavePillarFeatureConfig>) :
    Feature<LargeCavePillarFeatureConfig>(codec) {

    override fun place(context: FeaturePlaceContext<LargeCavePillarFeatureConfig>): Boolean {
        val structureWorldAccess = context.level()
        val origin = context.origin()
        val config = context.config()
        val random = context.random()

        if (!CavePillarHelper.canGenerate(structureWorldAccess, origin)) return false


        val optional = Column.scan(
            structureWorldAccess, origin,
            config.floorToCeilingSearchRange,
            { CavePillarHelper.canGenerate(it) },
            { it.`is`(config.canPlaceOn) }
        )
        if (optional.isEmpty || optional.get() !is Column.Range) return false


        val bounded = optional.get() as Column.Range
        if (bounded.height() < 4) return false


        val i = (bounded.height().toFloat() * config.maxColumnRadiusToCaveHeightRatio).toInt()
        val j = Mth.clamp(i, config.columnRadius.minValue, config.columnRadius.maxValue)
        val k = Mth.randomBetweenInclusive(random, config.columnRadius.minValue, j)
        val cavePillarGenerator = createGenerator(
            config,
            origin.atY(bounded.ceiling() - 1),
            false,
            random,
            k,
            config.stalactiteBluntness,
            config.heightScale
        )
        val cavePillarGenerator2 = createGenerator(
            config,
            origin.atY(bounded.floor() + 1),
            true,
            random,
            k,
            config.stalagmiteBluntness,
            config.heightScale
        )
        val windModifier =
            if (cavePillarGenerator.generateWind(config) && cavePillarGenerator2.generateWind(config))
                WindModifier(origin.y, random, config.windSpeed)
            else
                WindModifier.create()

        if (cavePillarGenerator.canGenerate(structureWorldAccess, windModifier)) {
            cavePillarGenerator.generate(structureWorldAccess, random, windModifier)
        }
        if (cavePillarGenerator2.canGenerate(structureWorldAccess, windModifier)) {
            cavePillarGenerator2.generate(structureWorldAccess, random, windModifier)
        }
        return true
    }

    internal class CavePillarGenerator(
        val config: LargeCavePillarFeatureConfig,
        private var pos: BlockPos,
        private val isStalagmite: Boolean,
        private var scale: Int,
        private val bluntness: Double,
        private val heightScale: Double,
    ) {
        private val baseScale: Int
            get() = this.scale(0.0f)

        @Suppress("unused")
        private val bottomY: Int
            get() {
                if (this.isStalagmite) {
                    return pos.y
                }
                return pos.y - this.baseScale
            }

        @Suppress("unused")
        private val topY: Int
            get() {
                if (!this.isStalagmite) {
                    return pos.y
                }
                return pos.y + this.baseScale
            }

        fun canGenerate(world: WorldGenLevel, wind: WindModifier): Boolean {
            while (this.scale > 1) {
                val mutable = pos.mutable()
                val i = min(10.0, baseScale.toDouble()).toInt()
                for (j in 0 until i) {
                    if (world.getBlockState(mutable).`is`(Blocks.LAVA)) {
                        return false
                    }
                    if (CavePillarHelper.canGenerateBase(world, wind.modify(mutable), this.scale)) {
                        this.pos = mutable
                        return true
                    }
                    mutable.move(if (this.isStalagmite) Direction.DOWN else Direction.UP)
                }
                this.scale /= 2
            }
            return false
        }

        private fun scale(height: Float): Int {
            return CavePillarHelper.scaleHeightFromRadius(
                height.toDouble(),
                scale.toDouble(), this.heightScale, this.bluntness
            ).toInt()
        }

        fun generate(world: WorldGenLevel, random: RandomSource, wind: WindModifier) {
            for (i in -this.scale..this.scale) {
                block1@ for (j in -this.scale..this.scale) {
                    var k = 0
                    val f = Mth.sqrt((i * i + j * j).toFloat())
                    if (f > scale.toFloat() || (scale(f).also {
                            k = it
                        }) <= 0) continue
                    if (random.nextFloat().toDouble() < 0.2) {
                        k = (k.toFloat() * Mth.randomBetween(random, 0.8f, 1.0f)).toInt()
                    }
                    val mutable = pos.offset(i, 0, j).mutable()
                    var bl = false
                    val l = if (this.isStalagmite) world.getHeight(
                        Heightmap.Types.WORLD_SURFACE_WG,
                        mutable.x,
                        mutable.z
                    ) else Int.MAX_VALUE
                    var m = 0
                    while (m < k && mutable.y < l) {
                        val blockPos = wind.modify(mutable)
                        if (CavePillarHelper.canGenerateOrLava(world, blockPos)) {
                            bl = true
                            world.setBlock(
                                blockPos,
                                config.mainBlock.getState(random, blockPos),
                                Block.UPDATE_CLIENTS
                            )
                        } else if (bl && world.getBlockState(blockPos)
                                .`is`(config.canPlaceOn)
                        ) continue@block1
                        mutable.move(if (this.isStalagmite) Direction.UP else Direction.DOWN)
                        ++m
                    }
                }
            }
        }

        fun generateWind(config: LargeCavePillarFeatureConfig): Boolean {
            return this.scale >= config.minRadiusForWind && this.bluntness >= config.minBluntnessForWind.toDouble()
        }

    }

    internal class WindModifier {
        private val y: Int
        private val wind: Vec3?

        constructor(y: Int, random: RandomSource, windSpeed: FloatProvider) {
            this.y = y
            val f = windSpeed.sample(random)
            val g = Mth.randomBetween(random, 0f, Math.PI.toFloat()).toDouble()
            this.wind = Vec3((Mth.cos(g) * f).toDouble(), 0.0, (Mth.sin(g) * f).toDouble())
        }

        private constructor() {
            this.y = 0
            this.wind = null
        }

        fun modify(pos: BlockPos): BlockPos {
            if (this.wind == null) {
                return pos
            }
            val i = this.y - pos.y
            val vec3d = wind.scale(i.toDouble())
            return pos.offset(Mth.floor(vec3d.x), 0, Mth.floor(vec3d.z))
        }

        companion object {

            fun create(): WindModifier = WindModifier()

        }
    }

    companion object {

        private fun createGenerator(
            config: LargeCavePillarFeatureConfig,
            pos: BlockPos,
            isStalagmite: Boolean,
            random: RandomSource,
            scale: Int,
            bluntness: FloatProvider,
            heightScale: FloatProvider,
        ): CavePillarGenerator {
            return CavePillarGenerator(
                config, pos, isStalagmite, scale,
                bluntness.sample(random).toDouble(),
                heightScale.sample(random).toDouble()
            )
        }

    }
}