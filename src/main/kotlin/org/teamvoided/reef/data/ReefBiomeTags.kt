package org.teamvoided.reef.data

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import org.teamvoided.reef.Reef.id
import org.teamvoided.reef.util.tag

object ReefBiomeTags {

    @JvmField
    val HAS_ERODED_PILLAR = biomeTag("has_eroded_pillar")

    @JvmField
    val HAS_VANILLA_ERODED_PILLAR = biomeTag("has_vanilla_eroded_pillar")

    @JvmField
    val HAS_VANILLA_ICEBERG = biomeTag("has_vanilla_iceberg")

    fun biomeTag(id: String): TagKey<Biome> = Registries.BIOME.tag(id(id))

}
