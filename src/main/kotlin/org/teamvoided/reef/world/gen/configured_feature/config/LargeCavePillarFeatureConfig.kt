package org.teamvoided.reef.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.HolderSet
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.float_provider.FloatProvider
import net.minecraft.util.math.int_provider.IntProvider
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider

data class LargeCavePillarFeatureConfig(
    val floorToCeilingSearchRange: Int,
    val columnRadius: IntProvider,
    val heightScale: FloatProvider,
    val maxColumnRadiusToCaveHeightRatio: Float,
    val stalactiteBluntness: FloatProvider,
    val stalagmiteBluntness: FloatProvider,
    val windSpeed: FloatProvider,
    val minRadiusForWind: Int,
    val minBluntnessForWind: Float,
    val mainBlock: BlockStateProvider,
    var canPlaceOn: HolderSet<Block>
) : FeatureConfig {
    companion object {
        val CODEC: Codec<LargeCavePillarFeatureConfig> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(30)
                            .forGetter { it.floorToCeilingSearchRange },
                        IntProvider.create(1, 60).fieldOf("column_radius").forGetter { it.columnRadius },
                        FloatProvider.createValidatedCodec(0.0f, 20.0f).fieldOf("height_scale")
                            .forGetter { it.heightScale },
                        Codec.floatRange(0.1f, 1.0f).fieldOf("max_column_radius_to_cave_height_ratio")
                            .forGetter { it.maxColumnRadiusToCaveHeightRatio },
                        FloatProvider.createValidatedCodec(0.1f, 10.0f).fieldOf("stalactite_bluntness")
                            .forGetter { it.stalactiteBluntness },
                        FloatProvider.createValidatedCodec(0.1f, 10.0f).fieldOf("stalagmite_bluntness")
                            .forGetter { it.stalagmiteBluntness },
                        FloatProvider.createValidatedCodec(0.0f, 2.0f).fieldOf("wind_speed").forGetter { it.windSpeed },
                        Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter { it.minRadiusForWind },
                        Codec.floatRange(0.0f, 5.0f)
                            .fieldOf("min_bluntness_for_wind").forGetter { it.minBluntnessForWind },
                        BlockStateProvider.TYPE_CODEC.fieldOf("main_block").forGetter { it.mainBlock },
                        RegistryCodecs.homogeneousList(RegistryKeys.BLOCK).fieldOf("can_place_on")
                            .forGetter { it.canPlaceOn }
                    )
                    .apply(instance, ::LargeCavePillarFeatureConfig)
            }
    }
}
