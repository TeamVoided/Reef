package org.teamvoided.reef.init

import com.mojang.serialization.MapCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
import org.teamvoided.reef.Reef
import org.teamvoided.reef.util.register
import org.teamvoided.reef.world.level.levelgen.structure.pools.ReferencePoolElement

object ReefStructurePoolElementType {

    val REFERENCE = register("reference_pool_element", ReferencePoolElement.CODEC)

    fun init() = Unit

    fun <P : StructurePoolElement> register(id: String, mapCodec: MapCodec<P>): StructurePoolElementType<P> {
        return BuiltInRegistries.STRUCTURE_POOL_ELEMENT.register(Reef.id(id), StructurePoolElementType { mapCodec })
    }

}