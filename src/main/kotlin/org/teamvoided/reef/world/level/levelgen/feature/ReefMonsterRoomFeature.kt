package org.teamvoided.reef.world.level.levelgen.feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.util.Util
import net.minecraft.world.RandomizableContainer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.SpawnerBlockEntity
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.structure.StructurePiece
import org.teamvoided.reef.Reef.log
import org.teamvoided.reef.world.level.levelgen.feature.config.ReefMonsterRoomFeatureConfig

// TODO re do this at some point
@Suppress("DEPRECATION")
class ReefMonsterRoomFeature(codec: Codec<ReefMonsterRoomFeatureConfig>) :
    Feature<ReefMonsterRoomFeatureConfig>(codec) {

    override fun place(context: FeaturePlaceContext<ReefMonsterRoomFeatureConfig>): Boolean {
        val config = context.config()
        var blockPos2: BlockPos
        var u: Int
        var t: Int
        val predicate = isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE)
        val blockPos = context.origin()
        val random = context.random()
        val world = context.level()
        val j = random.nextInt(2) + 2
        val k = -j - 1
        val l = j + 1
        val o = random.nextInt(2) + 2
        val p = -o - 1
        val q = o + 1
        var r = 0
        var s = k
        while (s <= l) {
            t = -1
            while (t <= 4) {
                u = p
                while (u <= q) {
                    blockPos2 = blockPos.offset(s, t, u)
                    val bl = world.getBlockState(blockPos2).isSolid
                    if (t == -1 && !bl) return false
                    if (t == 4 && !bl) return false

                    if (s != k && s != l && u != p && u != q || t != 0 || !world.isEmptyBlock(blockPos2) ||
                        !world.isEmptyBlock(blockPos2.above())
                    ) {
                        ++u
                        continue
                    }
                    ++r
                    ++u
                }
                ++t
            }
            ++s
        }
        if (r < 1 || r > 5) return false

        s = k
        while (s <= l) {
            t = 3
            while (t >= -1) {
                u = p
                while (u <= q) {
                    blockPos2 = blockPos.offset(s, t, u)
                    val blockState = world.getBlockState(blockPos2)
                    if (s == k || t == -1 || u == p || s == l || t == 4 || u == q) {
                        if (blockPos2.y >= world.minY && !world.getBlockState(blockPos2.below()).isSolid) {
                            world.setBlock(blockPos2, Blocks.CAVE_AIR.defaultBlockState(), Block.UPDATE_CLIENTS)
                            ++u
                            continue
                        }
                        if (!blockState.isSolid || blockState.`is`(Blocks.CHEST)) {
                            ++u
                            continue
                        }
                        if (t == -1 && random.nextInt(4) != 0) {
                            this.safeSetBlock(
                                world, blockPos2, config.secondaryBlock.getState(random, blockPos2), predicate
                            )
                            ++u
                            continue
                        }
                        this.safeSetBlock(
                            world, blockPos2, config.primaryBlock.getState(random, blockPos2), predicate
                        )
                        ++u
                        continue
                    }
                    if (blockState.`is`(Blocks.CHEST) || blockState.`is`(Blocks.SPAWNER)) {
                        ++u
                        continue
                    }
                    this.safeSetBlock(world, blockPos2, Blocks.CAVE_AIR.defaultBlockState(), predicate)
                    ++u
                }
                --t
            }
            ++s
        }
        s = 0
        block6@ while (s < 2) {
            t = 0
            while (t < 3) {
                @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE") var w: Int
                @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE") var v: Int
                u = blockPos.x + random.nextInt(j * 2 + 1) - j
                val blockPos3 = BlockPos(
                    u,
                    blockPos.y.also { v = it },
                    (blockPos.z + random.nextInt(o * 2 + 1) - o).also {
                        w = it
                    })
                if (!world.isEmptyBlock(blockPos3)) {
                    ++t
                    continue
                }
                var x = 0
                for (direction in Direction.Plane.HORIZONTAL) {
                    if (!world.getBlockState(blockPos3.relative(direction)).isSolid) continue
                    ++x
                }
                if (x != 1) {
                    ++t
                    continue
                }
                this.safeSetBlock(
                    world, blockPos3,
                    StructurePiece.reorient(world, blockPos3, Blocks.CHEST.defaultBlockState()), predicate
                )
                // LootTables.SIMPLE_DUNGEON_CHEST
                RandomizableContainer.setBlockEntityLootTable(
                    world, random, blockPos3, ResourceKey.create(Registries.LOOT_TABLE, config.lootTable)
                )
                ++s
                continue@block6
                ++t
            }
            ++s
        }
        this.safeSetBlock(world, blockPos, Blocks.SPAWNER.defaultBlockState(), predicate)
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is SpawnerBlockEntity) {
            blockEntity.setEntityId(Util.getRandom(config.monsterType, random), random)
        } else {
            log.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.x, blockPos.y, blockPos.z)
        }
        return true
    }

}