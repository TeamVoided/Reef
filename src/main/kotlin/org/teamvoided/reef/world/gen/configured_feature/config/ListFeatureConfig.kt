package org.teamvoided.reef.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.Holder
import net.minecraft.util.dynamic.Codecs
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.PlacedFeature

data class ListFeatureConfig(
    val tries: Int,
//        val spreadXz: Int,
//        val spreadY: Int,
    val features: List<Holder<PlacedFeature>>
) :
    FeatureConfig {
    companion object {
        val CODEC: Codec<ListFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter { it.tries },
//                        Codecs.NONNEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter { it.spreadXz },
//                        Codecs.NONNEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter { it.spreadY },
                    PlacedFeature.REGISTRY_CODEC.listOf().fieldOf("features").forGetter { it.features }
                ).apply(instance, ::ListFeatureConfig)
            }
    }
}
