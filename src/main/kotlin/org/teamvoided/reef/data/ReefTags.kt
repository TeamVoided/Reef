package org.teamvoided.reef.data

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.biome.Biome
import org.teamvoided.reef.Reef.id

object ReefTags {
    @JvmField
    val HAS_ERODED_PILLAR = biomeTag("has_eroded_pillar")
    @JvmField
    val HAS_ICEBERG = biomeTag("has_iceberg")


    private fun biomeTag(id: String): TagKey<Biome> = TagKey.of(RegistryKeys.BIOME, id(id))
    fun create(id: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id(id))

}