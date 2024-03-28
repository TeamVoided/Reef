package org.teamvoided.reef.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.HolderSet
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider

data class SpikeFeatureConfig(
    val chanceForLongSpike: Int,
    val longSpikeOffsetMin: Int,
    val longSpikeOffsetMax: Int,
    val baseBlock: BlockStateProvider,
    var canReplace: HolderSet<Block>

) : FeatureConfig {
    companion object {
        val CODEC: Codec<SpikeFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<SpikeFeatureConfig> ->
                instance.group(
                    Codec.INT.fieldOf("chance_for_long_spike").orElse(60).forGetter { it.chanceForLongSpike },
                    Codec.INT.fieldOf("long_spike_offset_min").orElse(10).forGetter { it.longSpikeOffsetMin },
                    Codec.INT.fieldOf("long_spike_offset_max").orElse(30).forGetter { it.longSpikeOffsetMax },
                    BlockStateProvider.TYPE_CODEC.fieldOf("base_block").forGetter { it.baseBlock },
                    RegistryCodecs.homogeneousList(RegistryKeys.BLOCK).fieldOf("can_replace")
                        .forGetter { it.canReplace }
                ).apply(instance, ::SpikeFeatureConfig)
            }
    }
}