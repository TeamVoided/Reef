package org.teamvoided.reef.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import org.teamvoided.reef.data.ReefTags
import java.util.concurrent.CompletableFuture

class BiomeTagGen(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Biome>(output, Registries.BIOME, registriesFuture) {
    override fun addTags(arg: HolderLookup.Provider) {
        builder(ReefTags.HAS_VANILLA_ERODED_PILLAR)
            .add(Biomes.ERODED_BADLANDS)
        /* builder(ReefTags.HAS_ERODED_PILLAR)
               .add(Biomes.FOREST)
               .add(Biomes.PLAINS)*/
        builder(ReefTags.HAS_VANILLA_ICEBERG)
            .add(Biomes.FROZEN_OCEAN)
            .add(Biomes.DEEP_FROZEN_OCEAN)
    }
}
