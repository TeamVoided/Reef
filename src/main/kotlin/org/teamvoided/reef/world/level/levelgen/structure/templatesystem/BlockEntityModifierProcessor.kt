package org.teamvoided.reef.world.level.levelgen.structure.templatesystem

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier
import org.teamvoided.reef.init.ReefStructureProcessorType

@Suppress("unused")
class BlockEntityModifierProcessor(val mods: List<Pair<RuleTest, RuleBlockEntityModifier>>) : StructureProcessor() {

    constructor(vararg pairs: Pair<RuleTest, RuleBlockEntityModifier>) : this(pairs.toList())

    override fun getType(): StructureProcessorType<*> = ReefStructureProcessorType.BLOCK_ENTITY_MODIFIER

    override fun processBlock(
        levelReader: LevelReader, blockPos: BlockPos, blockPos2: BlockPos,
        blockInfo: StructureBlockInfo, blockInfo2: StructureBlockInfo, placeSettings: StructurePlaceSettings,
    ): StructureBlockInfo {
        val random = RandomSource.create(Mth.getSeed(blockInfo2.pos()))

        for (pair in mods) {
            if (pair.first.test(blockInfo2.state(), random)) {
                return StructureBlockInfo(
                    blockInfo2.pos(),
                    blockInfo2.state(),
                    pair.second.apply(random, blockInfo2.nbt())
                )
            }
        }

        return blockInfo2
    }

    companion object {

        val CODEC: MapCodec<BlockEntityModifierProcessor> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.pair(
                    RuleTest.CODEC.fieldOf("input_predicate").codec(),
                    RuleBlockEntityModifier.CODEC.fieldOf("block_entity_modifier").codec()
                ).listOf().fieldOf("modifications").forGetter(BlockEntityModifierProcessor::mods)

            ).apply(it, ::BlockEntityModifierProcessor)
        }

    }
}