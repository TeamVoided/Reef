package org.teamvoided.reef.init

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
import org.teamvoided.reef.Reef
import org.teamvoided.reef.world.level.levelgen.structure.pools.ReferencePoolElement

object ReefStructurePoolElementType {
    fun init() = Unit
    val REFERENCE = register("reference_pool_element", ReferencePoolElement.CODEC)

    fun <P : StructurePoolElement> register(id: String, mapCodec: MapCodec<P>): StructurePoolElementType<P> =
        Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, Reef.id(id), StructurePoolElementType { mapCodec })

}