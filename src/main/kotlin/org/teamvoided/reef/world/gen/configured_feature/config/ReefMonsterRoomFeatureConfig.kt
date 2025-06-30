package org.teamvoided.reef.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

data class ReefMonsterRoomFeatureConfig(
    val primaryBlock: BlockStateProvider,
    val secondaryBlock: BlockStateProvider,
    val monsterType: List<EntityType<*>>,
    val lootTable: ResourceLocation,
) : FeatureConfiguration {
    companion object {
        val CODEC: Codec<ReefMonsterRoomFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<ReefMonsterRoomFeatureConfig> ->
                instance.group(
                    BlockStateProvider.CODEC.fieldOf("base_block").forGetter { it.primaryBlock },
                    BlockStateProvider.CODEC.fieldOf("secondary_block").forGetter { it.secondaryBlock },
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().fieldOf("monster_type")
                        .forGetter { it.monsterType },
                    ResourceLocation.CODEC.fieldOf("loot_table").forGetter { it.lootTable }
                ).apply(instance, ::ReefMonsterRoomFeatureConfig)
            }
    }
}
