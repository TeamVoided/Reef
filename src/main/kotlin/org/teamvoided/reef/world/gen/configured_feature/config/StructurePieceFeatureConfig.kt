package org.teamvoided.reef.world.gen.configured_feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.Holder
import net.minecraft.structure.processor.StructureProcessorList
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.Identifier
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.feature.FeatureConfig

data class StructurePieceFeatureConfig(
    val structures: List<Identifier>,
    val processors: Holder<StructureProcessorList>,
    val maxEmptyCorners: Int,
    val heightmap: Heightmap.Type
) : FeatureConfig {

    constructor(
        structure: Identifier, processors: Holder<StructureProcessorList>,
        maxEmptyCorners: Int, heightmap: Heightmap.Type
    ) : this(listOf(structure), processors, maxEmptyCorners, heightmap)

    init {
        require(structures.isNotEmpty()) { "Structure Piece structure lists need at least one entry" }
    }

    companion object {
        val CODEC: Codec<StructurePieceFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Identifier.CODEC.listOf().fieldOf("structures").forGetter { it.structures },
                    StructureProcessorType.REGISTRY_CODEC.fieldOf("processors").forGetter { it.processors },
                    Codec.intRange(0, 8).fieldOf("max_empty_corners_allowed").forGetter { it.maxEmptyCorners },
                    Heightmap.Type.CODEC.fieldOf("heightmap").forGetter { it.heightmap }
                ).apply(instance, ::StructurePieceFeatureConfig)
            }
    }
}
