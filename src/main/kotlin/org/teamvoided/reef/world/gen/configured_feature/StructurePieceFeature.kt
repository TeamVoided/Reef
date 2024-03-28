package org.teamvoided.reef.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Blocks
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import org.apache.commons.lang3.mutable.MutableInt
import org.teamvoided.reef.world.gen.configured_feature.config.StructurePieceFeatureConfig

class StructurePieceFeature(configCodec: Codec<StructurePieceFeatureConfig>?) :
    Feature<StructurePieceFeatureConfig>(configCodec) {

    override fun place(context: FeatureContext<StructurePieceFeatureConfig>): Boolean {
        val random = context.random
        val structureWorldAccess = context.world
        val blockPos = context.origin
        val blockRotation = BlockRotation.random(random)
        val config = context.config

        val structureTemplateManager = structureWorldAccess.toServerWorld().server.structureTemplateManager
        val structure =
            structureTemplateManager.getStructureOrBlank(config.structures[random.nextInt(config.structures.size)])
        val chunkPos = ChunkPos(blockPos)
        val blockBox = BlockBox(
            chunkPos.startX - 16,
            structureWorldAccess.bottomY,
            chunkPos.startZ - 16,
            chunkPos.endX + 16,
            structureWorldAccess.topY,
            chunkPos.endZ + 16
        )
        val structurePlacementData =
            StructurePlacementData().setRotation(blockRotation).setBoundingBox(blockBox).setRandom(random)
        val vec3i = structure.getRotatedSize(blockRotation)
        val blockPos2 = blockPos.add(-vec3i.x / 2, 0, -vec3i.z / 2)

        if (getEmptyCorners(
                structureWorldAccess,
                structure.calculateBoundingBox(structurePlacementData, blockPos2)
            ) > config.maxEmptyCorners
        ) return false

        structurePlacementData.clearProcessors()
        config.processors.value().list.forEach(structurePlacementData::addProcessor)
        structure.place(structureWorldAccess, blockPos2, blockPos2, structurePlacementData, random, 4)
        return true
    }

    private fun getEmptyCorners(world: StructureWorldAccess, box: BlockBox): Int {
        val mutableInt = MutableInt(0)
        box.forEachVertex {
            val blockState = world.getBlockState(it)
            if (blockState.isAir || blockState.isOf(Blocks.LAVA) || blockState.isOf(Blocks.WATER)) {
                mutableInt.add(1)
            }
        }
        return mutableInt.value
    }


}