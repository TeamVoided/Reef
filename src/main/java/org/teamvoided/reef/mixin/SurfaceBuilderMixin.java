package org.teamvoided.reef.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.reef.api.events.CustomSurfaceBuilder;
import org.teamvoided.reef.data.ReefTags;

@Mixin(SurfaceSystem.class)
public abstract class SurfaceBuilderMixin {
    @Final
    @Shadow
    private NormalNoise badlandsSurfaceNoise;
    @Final
    @Shadow
    private NormalNoise badlandsPillarNoise;
    @Shadow
    @Final
    private NormalNoise badlandsPillarRoofNoise;
    @Final
    @Shadow
    private BlockState defaultBlock;
    @Final
    @Shadow
    private int seaLevel;


    @Redirect(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/resources/ResourceKey;)Z"))
    private boolean reef$taggedVanillaSurfaceBuilders(Holder<Biome> biome, ResourceKey<Biome> biomeKey) {
        if (biomeKey == Biomes.ERODED_BADLANDS) return biome.is(ReefTags.HAS_ERODED_PILLAR);
        else if (biomeKey == Biomes.FROZEN_OCEAN || biomeKey == Biomes.DEEP_FROZEN_OCEAN)
            return biome.is(ReefTags.HAS_VANILLA_ICEBERG);
        else return biome.is(biomeKey);
    }

    @Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I", ordinal = 1))
    private void reef$customSurfaceBuildersBefore(RandomState randomState, BiomeManager biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, WorldGenerationContext context, ChunkAccess chunk, NoiseChunk chunkNoiseSampler, SurfaceRules.RuleSource surfaceRule, CallbackInfo ci,
                                                  @Local Holder<Biome> biome, @Local(ordinal = 4) int x, @Local(ordinal = 5) int z, @Local BlockColumn chunkBlockColumn) {
        CustomSurfaceBuilder.PRE_RULES.invoker().register(randomState, defaultBlock, seaLevel, biomeAccess, chunk, chunkBlockColumn, x, z);

        // Move this to kotlin in the future
        if (biome.is(ReefTags.HAS_ERODED_PILLAR)) {
            int y = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) + 1;
            double sn = this.badlandsSurfaceNoise.getValue(x, 0.0, z);
            double pn = this.badlandsPillarNoise.getValue(x * 0.2, 0.0, z * 0.2);
            double e = Math.min(Math.abs(sn * 8.25), pn * 15.0);
            if (!(e <= 0.0)) {
                double h = Math.abs(this.badlandsPillarRoofNoise.getValue((double) x * 0.75, 0.0, (double) z * 0.75) * 1.5);
                int j = Mth
                        .floor(64.0 + Math.min(e * e * 2.5, Math.ceil(h * 50.0) + 24.0)) - Math.max(seaLevel - y, 0);
                if (y <= j) {
                    for (int k = j; k >= chunk.getMinY() && chunkBlockColumn.getBlock(k).is(BlockTags.REPLACEABLE); --k) {
                        chunkBlockColumn.setBlock(k, this.defaultBlock);
                    }
                }
            }
        }
    }

    @Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/resources/ResourceKey;)Z", ordinal = 1))
    private void reef$customSurfaceBuildersAfter(RandomState randomState, BiomeManager biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, WorldGenerationContext context, ChunkAccess chunk, NoiseChunk chunkNoiseSampler, SurfaceRules.RuleSource surfaceRule,
                                                 CallbackInfo ci, @Local(ordinal = 4) int x, @Local(ordinal = 5) int z, @Local BlockColumn chunkBlockColumn) {
        CustomSurfaceBuilder.POST_RULES.invoker().register(randomState, defaultBlock, seaLevel, biomeAccess, chunk, chunkBlockColumn, x, z);

    }
}