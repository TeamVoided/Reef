package org.teamvoided.reef.datagen.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import org.teamvoided.reef.data.ReefBiomeTags
import java.util.concurrent.CompletableFuture

class BiomeTagGen(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Biome>(output, Registries.BIOME, registriesFuture) {

    override fun addTags(arg: HolderLookup.Provider) {
        builder(ReefBiomeTags.HAS_VANILLA_ERODED_PILLAR)
            .add(Biomes.ERODED_BADLANDS)
        /*builder(ReefBiomeTags.HAS_ERODED_PILLAR)
            .add(Biomes.FOREST)
            .add(Biomes.PLAINS)*/
        builder(ReefBiomeTags.HAS_VANILLA_ICEBERG)
            .add(Biomes.FROZEN_OCEAN)
            .add(Biomes.DEEP_FROZEN_OCEAN)
    }

}