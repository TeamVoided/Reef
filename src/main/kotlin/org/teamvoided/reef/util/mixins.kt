package org.teamvoided.reef.util

import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams

fun fillBookshelfFromLootTable(be: ChiseledBookShelfBlockEntity, key: ResourceKey<LootTable>, seed: Long) {
    if (!be.isEmpty) return
    val level = be.level as? ServerLevel ?: return
    val table = level.server.reloadableRegistries()?.getLootTable(key) ?: return

    val lootParams = LootParams.Builder(level)
        .withParameter(LootContextParams.ORIGIN, be.blockPos.center)
        .create(LootContextParamSets.CHEST)

    table.fill(be, lootParams, seed)
}