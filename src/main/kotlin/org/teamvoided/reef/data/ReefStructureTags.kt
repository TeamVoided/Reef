package org.teamvoided.reef.data

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.levelgen.structure.Structure
import org.teamvoided.reef.Reef

object ReefStructureTags {

    @JvmField
    val HAS_INJECTED_PROCESSOR_LISTS = create("has_injected_processor_lists")

    fun create(id: String): TagKey<Structure> = TagKey.create(Registries.STRUCTURE, Reef.id(id))

}