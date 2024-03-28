package org.teamvoided.reef.world.gen.configured_feature

import com.mojang.serialization.Codec
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.MobSpawnerBlockEntity
import net.minecraft.inventory.LootableInventory
import net.minecraft.registry.tag.BlockTags
import net.minecraft.structure.piece.StructurePiece
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import org.teamvoided.reef.Reef.log
import org.teamvoided.reef.world.gen.configured_feature.config.MonsterRoomFeatureConfig

class MonsterRoomFeature(codec: Codec<MonsterRoomFeatureConfig>) :
    Feature<MonsterRoomFeatureConfig>(codec) {
    override fun place(context: FeatureContext<MonsterRoomFeatureConfig>): Boolean {
        val config = context.config
        var blockPos2: BlockPos
        var u: Int
        var t: Int
        val predicate = notInBlockTagPredicate(BlockTags.FEATURES_CANNOT_REPLACE)
        val blockPos = context.origin
        val random = context.random
        val world = context.world
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
                    blockPos2 = blockPos.add(s, t, u)
                    val bl = world.getBlockState(blockPos2).isSolid
                    if (t == -1 && !bl) return false
                    if (t == 4 && !bl) return false

                    if (s != k && s != l && u != p && u != q || t != 0 || !world.isAir(blockPos2) ||
                        !world.isAir(blockPos2.up())
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
                    blockPos2 = blockPos.add(s, t, u)
                    val blockState = world.getBlockState(blockPos2)
                    if (s == k || t == -1 || u == p || s == l || t == 4 || u == q) {
                        if (blockPos2.y >= world.bottomY && !world.getBlockState(blockPos2.down()).isSolid) {
                            world.setBlockState(blockPos2, Blocks.CAVE_AIR.defaultState, Block.NOTIFY_LISTENERS)
                            ++u
                            continue
                        }
                        if (!blockState.isSolid || blockState.isOf(Blocks.CHEST)) {
                            ++u
                            continue
                        }
                        if (t == -1 && random.nextInt(4) != 0) {
                            this.setBlockStateIf(
                                world, blockPos2, config.secondaryBlock.getBlockState(random, blockPos2), predicate
                            )
                            ++u
                            continue
                        }
                        this.setBlockStateIf(
                            world, blockPos2, config.primaryBlock.getBlockState(random, blockPos2), predicate
                        )
                        ++u
                        continue
                    }
                    if (blockState.isOf(Blocks.CHEST) || blockState.isOf(Blocks.SPAWNER)) {
                        ++u
                        continue
                    }
                    this.setBlockStateIf(world, blockPos2, Blocks.CAVE_AIR.defaultState, predicate)
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
                var w: Int
                var v: Int
                u = blockPos.x + random.nextInt(j * 2 + 1) - j
                val blockPos3 = BlockPos(
                    u,
                    blockPos.y.also { v = it },
                    (blockPos.z + random.nextInt(o * 2 + 1) - o).also {
                        w = it
                    })
                if (!world.isAir(blockPos3)) {
                    ++t
                    continue
                }
                var x = 0
                for (direction in Direction.Type.HORIZONTAL) {
                    if (!world.getBlockState(blockPos3.offset(direction)).isSolid) continue
                    ++x
                }
                if (x != 1) {
                    ++t
                    continue
                }
                this.setBlockStateIf(
                    world, blockPos3,
                    StructurePiece.orientateChest(world, blockPos3, Blocks.CHEST.defaultState), predicate
                )
                // LootTables.SIMPLE_DUNGEON_CHEST
                LootableInventory.setupLootTable(world, random, blockPos3, config.lootTable)
                ++s
                continue@block6
                ++t
            }
            ++s
        }
        this.setBlockStateIf(world, blockPos, Blocks.SPAWNER.defaultState, predicate)
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is MobSpawnerBlockEntity) {
            blockEntity.method_46408(Util.getRandom(config.monsterType, random), random)
        } else {
            log.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockPos.x, blockPos.y, blockPos.z)
        }
        return true
    }
}


