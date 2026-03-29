package org.teamvoided.reef.world.level.levelgen.feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

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
    var canPlaceOn: HolderSet<Block>,
) : FeatureConfiguration {

    companion object {

        val CODEC: Codec<LargeCavePillarFeatureConfig> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(30)
                        .forGetter { it.floorToCeilingSearchRange },
                    IntProvider.codec(1, 60).fieldOf("column_radius").forGetter { it.columnRadius },
                    FloatProvider.codec(0.0f, 20.0f).fieldOf("height_scale")
                        .forGetter { it.heightScale },
                    Codec.floatRange(0.1f, 1.0f).fieldOf("max_column_radius_to_cave_height_ratio")
                        .forGetter { it.maxColumnRadiusToCaveHeightRatio },
                    FloatProvider.codec(0.1f, 10.0f).fieldOf("stalactite_bluntness")
                        .forGetter { it.stalactiteBluntness },
                    FloatProvider.codec(0.1f, 10.0f).fieldOf("stalagmite_bluntness")
                        .forGetter { it.stalagmiteBluntness },
                    FloatProvider.codec(0.0f, 2.0f).fieldOf("wind_speed").forGetter { it.windSpeed },
                    Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter { it.minRadiusForWind },
                    Codec.floatRange(0.0f, 5.0f)
                        .fieldOf("min_bluntness_for_wind").forGetter { it.minBluntnessForWind },
                    BlockStateProvider.CODEC.fieldOf("main_block").forGetter { it.mainBlock },
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_place_on")
                        .forGetter { it.canPlaceOn }
                )
                .apply(instance, ::LargeCavePillarFeatureConfig)
        }

    }
}