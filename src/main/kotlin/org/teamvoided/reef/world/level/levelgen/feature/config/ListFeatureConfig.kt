package org.teamvoided.reef.world.level.levelgen.feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature

data class ListFeatureConfig(
    val tries: Int,
//        val spreadXz: Int,
//        val spreadY: Int,
    val features: List<Holder<PlacedFeature>>,
) :
    FeatureConfiguration {
    companion object {
        val CODEC: Codec<ListFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter { it.tries },
//                        Codecs.NONNEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter { it.spreadXz },
//                        Codecs.NONNEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter { it.spreadY },
                    PlacedFeature.CODEC.listOf().fieldOf("features").forGetter { it.features }
                ).apply(instance, ::ListFeatureConfig)
            }
    }
}
