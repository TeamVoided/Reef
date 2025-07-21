package org.teamvoided.reef.world.level.levelgen.structure.pools

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.Vec3i
import net.minecraft.util.RandomSource
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager
import org.teamvoided.reef.init.ReefStructurePoolElementType
import kotlin.math.max

open class ReferencePoolElement(
    val templatePool: Holder<StructureTemplatePool>, projection: StructureTemplatePool.Projection,
) : StructurePoolElement(projection) {

    fun template(): StructureTemplatePool = templatePool.value()

    override fun getSize(structureTemplateManager: StructureTemplateManager, rotation: Rotation): Vec3i {
        var x = 0
        var y = 0
        var z = 0

        for (element in template().templates) {
            val vec3i = element.first.getSize(structureTemplateManager, rotation)
            x = max(x, vec3i.x)
            y = max(y, vec3i.y)
            z = max(z, vec3i.z)
        }


        return Vec3i(x, y, z)
    }

    override fun getShuffledJigsawBlocks(
        structureTemplateManager: StructureTemplateManager, blockPos: BlockPos,
        rotation: Rotation, randomSource: RandomSource,
    ): MutableList<StructureTemplate.JigsawBlockInfo> = template().templates.first().first
        .getShuffledJigsawBlocks(structureTemplateManager, blockPos, rotation, randomSource)


    override fun getBoundingBox(
        structureTemplateManager: StructureTemplateManager,
        blockPos: BlockPos,
        rotation: Rotation,
    ): BoundingBox {
        val list = template().templates.map { it.first }
            .filter { it !== EmptyPoolElement.INSTANCE }
            .map { it.getBoundingBox(structureTemplateManager, blockPos, rotation) }
        return BoundingBox.encapsulatingBoxes(list)
            .orElseThrow { IllegalStateException("Unable to calculate boundingbox for ReferencePoolElement") }
    }

    override fun place(
        structureTemplateManager: StructureTemplateManager,
        worldGenLevel: WorldGenLevel,
        structureManager: StructureManager,
        chunkGenerator: ChunkGenerator,
        blockPos: BlockPos,
        blockPos2: BlockPos,
        rotation: Rotation,
        boundingBox: BoundingBox,
        randomSource: RandomSource,
        liquidSettings: LiquidSettings,
        bl: Boolean,
    ): Boolean {
        val random = randomSource.fork()
        random.setSeed(blockPos.asLong())
        return template().getRandomTemplate(random).place(
            structureTemplateManager,
            worldGenLevel,
            structureManager,
            chunkGenerator,
            blockPos,
            blockPos2,
            rotation,
            boundingBox,
            random,
            liquidSettings,
            bl
        )
    }

    override fun getType(): StructurePoolElementType<ReferencePoolElement> = ReefStructurePoolElementType.REFERENCE
    override fun setProjection(projection: StructureTemplatePool.Projection): StructurePoolElement {
        super.setProjection(projection)
        return this
    }

    override fun toString(): String = "Reference[${this.templatePool}]"

    companion object {
        val CODEC: MapCodec<ReferencePoolElement> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                StructureTemplatePool.CODEC.fieldOf("reference").forGetter { it.templatePool },
                projectionCodec()
            ).apply(instance, ::ReferencePoolElement)
        }
    }
}