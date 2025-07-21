package org.teamvoided.reef.world.level.levelgen.feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

data class SpikeFeatureConfig(
    val chanceForLongSpike: Int,
    val longSpikeOffsetMin: Int,
    val longSpikeOffsetMax: Int,
    val baseBlock: BlockStateProvider,
    var canReplace: HolderSet<Block>,

    ) : FeatureConfiguration {
    companion object {
        val CODEC: Codec<SpikeFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<SpikeFeatureConfig> ->
                instance.group(
                    Codec.INT.fieldOf("chance_for_long_spike").orElse(60).forGetter { it.chanceForLongSpike },
                    Codec.INT.fieldOf("long_spike_offset_min").orElse(10).forGetter { it.longSpikeOffsetMin },
                    Codec.INT.fieldOf("long_spike_offset_max").orElse(30).forGetter { it.longSpikeOffsetMax },
                    BlockStateProvider.CODEC.fieldOf("base_block").forGetter { it.baseBlock },
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_replace")
                        .forGetter { it.canReplace }
                ).apply(instance, ::SpikeFeatureConfig)
            }
    }
}