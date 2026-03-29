package org.teamvoided.reef.init

import com.mojang.serialization.MapCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
import org.teamvoided.reef.Reef.id
import org.teamvoided.reef.util.register
import org.teamvoided.reef.world.level.levelgen.structure.templatesystem.BlockEntityModifierProcessor

object ReefStructureProcessorType {

    val BLOCK_ENTITY_MODIFIER = register("block_entity_modifier", BlockEntityModifierProcessor.CODEC)

    fun init() = Unit

    fun <P : StructureProcessor> register(id: String, mapCodec: MapCodec<P>): StructureProcessorType<P> {
        return BuiltInRegistries.STRUCTURE_PROCESSOR.register(id(id), StructureProcessorType { mapCodec })
    }

}