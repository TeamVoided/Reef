package org.teamvoided.reef.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import org.apache.commons.lang3.mutable.MutableInt
import org.teamvoided.reef.world.gen.configured_feature.config.StructurePieceFeatureConfig

class StructurePieceFeature(configCodec: Codec<StructurePieceFeatureConfig>?) :
    Feature<StructurePieceFeatureConfig>(configCodec) {

    override fun place(context: FeaturePlaceContext<StructurePieceFeatureConfig>): Boolean {
        val random = context.random()
        val structureWorldAccess = context.level()
        val blockPos = context.origin()
        val blockRotation = Rotation.getRandom(random)
        val config = context.config()

        val structureManager = structureWorldAccess.server!!.structureManager
        val structure = structureManager.getOrCreate(config.structures[random.nextInt(config.structures.size)])
        val chunkPos = ChunkPos(blockPos)
        val blockBox = BoundingBox(
            chunkPos.minBlockX - 16,
            structureWorldAccess.minY,
            chunkPos.minBlockZ - 16,
            chunkPos.maxBlockX + 16,
            structureWorldAccess.maxY,
            chunkPos.maxBlockZ + 16
        )
        val structurePlacementData =
            StructurePlaceSettings().setRotation(blockRotation).setBoundingBox(blockBox).setRandom(random)
        val vec3i = structure.getSize(blockRotation)
        val blockPos2 = blockPos.offset(-vec3i.x / 2, 0, -vec3i.z / 2)

        if (getEmptyCorners(
                structureWorldAccess,
                structure.getBoundingBox(structurePlacementData, blockPos2)
            ) > config.maxEmptyCorners
        ) return false

        structurePlacementData.clearProcessors()
        config.processors.value().list().forEach(structurePlacementData::addProcessor)
        structure.placeInWorld(structureWorldAccess, blockPos2, blockPos2, structurePlacementData, random, 4)
        return true
    }

    private fun getEmptyCorners(world: WorldGenLevel, box: BoundingBox): Int {
        val mutableInt = MutableInt(0)
        box.forAllCorners {
            val blockState = world.getBlockState(it)
            if (blockState.isAir || blockState.`is`(Blocks.LAVA) || blockState.`is`(Blocks.WATER)) {
                mutableInt.add(1)
            }
        }
        return mutableInt.value
    }


}