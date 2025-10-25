package org.teamvoided.reef.data

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import org.teamvoided.reef.Reef.id

object ReefBiomeTags {
    @JvmField
    val HAS_ERODED_PILLAR = biomeTag("has_eroded_pillar")

    @JvmField
    val HAS_VANILLA_ERODED_PILLAR = biomeTag("has_vanilla_eroded_pillar")

    @JvmField
    val HAS_VANILLA_ICEBERG = biomeTag("has_vanilla_iceberg")


    private fun biomeTag(id: String): TagKey<Biome> = TagKey.create(Registries.BIOME, id(id))
    fun create(id: String): TagKey<Block> = TagKey.create(Registries.BLOCK, id(id))
}
