package org.teamvoided.reef.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.EntityType
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.stateprovider.BlockStateProvider

data class MonsterRoomFeatureConfig(
    val primaryBlock: BlockStateProvider,
    val secondaryBlock: BlockStateProvider,
    val monsterType: List<EntityType<*>>,
    val lootTable: Identifier
    ) : FeatureConfig {
    companion object {
        val CODEC: Codec<MonsterRoomFeatureConfig> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<MonsterRoomFeatureConfig> ->
                instance.group(
                    BlockStateProvider.TYPE_CODEC.fieldOf("base_block").forGetter { it.primaryBlock },
                    BlockStateProvider.TYPE_CODEC.fieldOf("secondary_block").forGetter { it.secondaryBlock },
                    Registries.ENTITY_TYPE.codec.listOf().fieldOf("monster_type").forGetter { it.monsterType },
                    Identifier.CODEC.fieldOf("loot_table").forGetter { it.lootTable }
                ).apply(instance, ::MonsterRoomFeatureConfig)
            }
    }
}