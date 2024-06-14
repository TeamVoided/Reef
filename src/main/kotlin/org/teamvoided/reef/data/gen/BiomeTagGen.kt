package org.teamvoided.reef.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import org.teamvoided.reef.data.ReefTags
import java.util.concurrent.CompletableFuture

class BiomeTagGen(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<Biome>(output, RegistryKeys.BIOME, registriesFuture) {
    override fun configure(arg: HolderLookup.Provider?) {
        getOrCreateTagBuilder(ReefTags.HAS_VANILLA_ERODED_PILLAR)
            .add(Biomes.ERODED_BADLANDS)
        getOrCreateTagBuilder(ReefTags.HAS_ICEBERG)
            .add(Biomes.FROZEN_OCEAN)
            .add(Biomes.DEEP_FROZEN_OCEAN)
    }
}
