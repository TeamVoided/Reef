package org.teamvoided.reef.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.Heightmap
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.CaveSurface
import net.minecraft.world.gen.feature.util.CaveSurface.Bounded
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.reef.world.gen.configured_feature.config.LargeCavePillarFeatureConfig
import kotlin.math.min

class LargeCavePillarFeature(codec: Codec<LargeCavePillarFeatureConfig>) :
    Feature<LargeCavePillarFeatureConfig>(codec) {
    override fun place(context: FeatureContext<LargeCavePillarFeatureConfig>): Boolean {
        val structureWorldAccess = context.world
        val origin = context.origin
        val config = context.config
        val random = context.random

        if (!CavePillarHelper.canGenerate(structureWorldAccess, origin)) return false


        val optional = CaveSurface.create(
            structureWorldAccess, origin,
            config.floorToCeilingSearchRange,
            { CavePillarHelper.canGenerate(it) },
            { it.isIn(config.canPlaceOn) }
        )
        if (optional.isEmpty || optional.get() !is Bounded) return false


        val bounded = optional.get() as Bounded
        if (bounded.height < 4) return false


        val i = (bounded.height.toFloat() * config.maxColumnRadiusToCaveHeightRatio).toInt()
        val j = MathHelper.clamp(i, config.columnRadius.min, config.columnRadius.max)
        val k = MathHelper.nextBetween(random, config.columnRadius.min, j)
        val cavePillarGenerator = createGenerator(
            config,
            origin.withY(bounded.ceiling - 1),
            false,
            random,
            k,
            config.stalactiteBluntness,
            config.heightScale
        )
        val cavePillarGenerator2 = createGenerator(
            config,
            origin.withY(bounded.floor + 1),
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
        private val heightScale: Double
    ) {
        private val baseScale: Int
            get() = this.scale(0.0f)

        private val bottomY: Int
            get() {
                if (this.isStalagmite) {
                    return pos.y
                }
                return pos.y - this.baseScale
            }

        private val topY: Int
            get() {
                if (!this.isStalagmite) {
                    return pos.y
                }
                return pos.y + this.baseScale
            }

        fun canGenerate(world: StructureWorldAccess, wind: WindModifier): Boolean {
            while (this.scale > 1) {
                val mutable = pos.mutableCopy()
                val i = min(10.0, baseScale.toDouble()).toInt()
                for (j in 0 until i) {
                    if (world.getBlockState(mutable).isOf(Blocks.LAVA)) {
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

        fun generate(world: StructureWorldAccess, random: RandomGenerator, wind: WindModifier) {
            for (i in -this.scale..this.scale) {
                block1@ for (j in -this.scale..this.scale) {
                    var k = 0
                    val f = MathHelper.sqrt((i * i + j * j).toFloat())
                    if (f > scale.toFloat() || (scale(f).also {
                            k = it
                        }) <= 0) continue
                    if (random.nextFloat().toDouble() < 0.2) {
                        k = (k.toFloat() * MathHelper.nextBetween(random, 0.8f, 1.0f)).toInt()
                    }
                    val mutable = pos.add(i, 0, j).mutableCopy()
                    var bl = false
                    val l = if (this.isStalagmite) world.getTopY(
                        Heightmap.Type.WORLD_SURFACE_WG,
                        mutable.x,
                        mutable.z
                    ) else Int.MAX_VALUE
                    var m = 0
                    while (m < k && mutable.y < l) {
                        val blockPos = wind.modify(mutable)
                        if (CavePillarHelper.canGenerateOrLava(world, blockPos)) {
                            bl = true
                            world.setBlockState(
                                blockPos,
                                config.mainBlock.getBlockState(random, blockPos),
                                Block.NOTIFY_LISTENERS
                            )
                        } else if (bl && world.getBlockState(blockPos)
                                .isIn(config.canPlaceOn)
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
        private val wind: Vec3d?

        constructor(y: Int, random: RandomGenerator?, windSpeed: FloatProvider) {
            this.y = y
            val f = windSpeed[random]
            val g = MathHelper.nextBetween(random, 0.0f, Math.PI.toFloat())
            this.wind = Vec3d((MathHelper.cos(g) * f).toDouble(), 0.0, (MathHelper.sin(g) * f).toDouble())
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
            val vec3d = wind.multiply(i.toDouble())
            return pos.add(MathHelper.floor(vec3d.x), 0, MathHelper.floor(vec3d.z))
        }

        companion object {
            fun create(): WindModifier {
                return WindModifier()
            }
        }
    }

    companion object {
        private fun createGenerator(
            config: LargeCavePillarFeatureConfig,
            pos: BlockPos,
            isStalagmite: Boolean,
            random: RandomGenerator,
            scale: Int,
            bluntness: FloatProvider,
            heightScale: FloatProvider
        ): CavePillarGenerator {
            return CavePillarGenerator(
                config, pos, isStalagmite, scale,
                bluntness[random].toDouble(),
                heightScale[random].toDouble()
            )
        }
    }
}