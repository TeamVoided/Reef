package org.teamvoided.reef.init

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import org.teamvoided.reef.Reef.id
import org.teamvoided.reef.world.gen.configured_feature.*
import org.teamvoided.reef.world.gen.configured_feature.config.*


@Suppress("unused")
object ReefFeatures {

    val SPIKE = register("spike", SpikeFeature(SpikeFeatureConfig.CODEC))
    val INVERTED_SPIKE = register("inverted_spike", InvertedSpikeFeature(SpikeFeatureConfig.CODEC))
    val MONSTER_ROOM = register("monster_room", MonsterRoomFeature(MonsterRoomFeatureConfig.CODEC))
    val STRUCTURE_PIECE = register("structure_piece", StructurePieceFeature(StructurePieceFeatureConfig.CODEC))
    val LARGE_CAVE_PILLAR = register("large_cave_pillar", LargeCavePillarFeature(LargeCavePillarFeatureConfig.CODEC))
    val FEATURE_LIST = register("feature_list", ListFeature(ListFeatureConfig.CODEC))

    fun init() {}
    private fun <C : FeatureConfig?, F : Feature<C>> register(name: String, feature: F): F =
        Registry.register(Registries.FEATURE, id(name), feature)
}