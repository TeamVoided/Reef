package org.teamvoided.reef.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import org.teamvoided.reef.Reef.id
import org.teamvoided.reef.util.register
import org.teamvoided.reef.world.level.levelgen.feature.*
import org.teamvoided.reef.world.level.levelgen.feature.config.*


@Suppress("unused")
object ReefFeatures {

    val SPIKE = register("spike", SpikeFeature(SpikeFeatureConfig.CODEC))
    val INVERTED_SPIKE = register("inverted_spike", InvertedSpikeFeature(SpikeFeatureConfig.CODEC))
    val MONSTER_ROOM = register("monster_room", ReefMonsterRoomFeature(ReefMonsterRoomFeatureConfig.CODEC))
    val STRUCTURE_PIECE = register("structure_piece", StructurePieceFeature(StructurePieceFeatureConfig.CODEC))
    val LARGE_CAVE_PILLAR = register("large_cave_pillar", LargeCavePillarFeature(LargeCavePillarFeatureConfig.CODEC))
    val FEATURE_LIST = register("feature_list", ListFeature(ListFeatureConfig.CODEC))

    fun init() = Unit

    fun <C : FeatureConfiguration, F : Feature<C>> register(name: String, feature: F): F {
        return BuiltInRegistries.FEATURE.register(id(name), feature)
    }

}
