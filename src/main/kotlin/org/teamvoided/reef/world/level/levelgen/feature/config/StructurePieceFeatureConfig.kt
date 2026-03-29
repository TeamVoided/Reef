package org.teamvoided.reef.world.level.levelgen.feature.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType

data class StructurePieceFeatureConfig(
    val structures: List<Identifier>,
    val processors: Holder<StructureProcessorList>,
    val maxEmptyCorners: Int,
    val heightmap: Heightmap.Types,
) : FeatureConfiguration {

    constructor(
        structure: Identifier, processors: Holder<StructureProcessorList>,
        maxEmptyCorners: Int, heightmap: Heightmap.Types,
    ) : this(listOf(structure), processors, maxEmptyCorners, heightmap)

    init {
        require(structures.isNotEmpty()) { "Structure Piece structure lists need at least one entry" }
    }

    companion object {

        val CODEC: Codec<StructurePieceFeatureConfig> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.listOf().fieldOf("structures").forGetter { it.structures },
                StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter { it.processors },
                Codec.intRange(0, 8).fieldOf("max_empty_corners_allowed").forGetter { it.maxEmptyCorners },
                Heightmap.Types.CODEC.fieldOf("heightmap").forGetter { it.heightmap }
            ).apply(instance, ::StructurePieceFeatureConfig)
        }

    }
}
