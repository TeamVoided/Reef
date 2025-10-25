package org.teamvoided.reef.init

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
import org.teamvoided.reef.Reef.id
import org.teamvoided.reef.world.level.levelgen.structure.templatesystem.BlockEntityModifierProcessor

object ReefStructureProcessorType {
    fun init() = Unit

    val BLOCK_ENTITY_MODIFIER = register("block_entity_modifier", BlockEntityModifierProcessor.CODEC)

    fun <P : StructureProcessor> register(id: String, mapCodec: MapCodec<P>): StructureProcessorType<P> =
        Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, id(id), StructureProcessorType { mapCodec })

}