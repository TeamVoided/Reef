package org.teamvoided.reef.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.RandomState;
import net.minecraft.world.gen.chunk.BlockColumn;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceRules;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.reef.data.ReefTags;
import org.teamvoided.reef.events.CustomSurfaceBuilder;

@Debug(export = true)
@Mixin(SurfaceBuilder.class)
public abstract class SurfaceBuilderMixin {
    @Final
    @Shadow
    private DoublePerlinNoiseSampler badlandsSurfaceNoise;
    @Final
    @Shadow
    private DoublePerlinNoiseSampler badlandsPillarNoise;
    @Final
    @Shadow
    private DoublePerlinNoiseSampler badlandsPillarRootNoise;
    @Final
    @Shadow
    private BlockState defaultBlock;
    @Final
    @Shadow
    private int seaLevel;

    @Redirect(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Holder;isRegistryKey(Lnet/minecraft/registry/RegistryKey;)Z"))
    private boolean reef$tagedVanillaSurfaceBuilders(Holder<Biome> biome, RegistryKey<Biome> biomeKey) {
        if (biomeKey == Biomes.ERODED_BADLANDS) return biome.isIn(ReefTags.HAS_ERODED_PILLAR);
        else if (biomeKey == Biomes.FROZEN_OCEAN || biomeKey == Biomes.DEEP_FROZEN_OCEAN)
            return biome.isIn(ReefTags.HAS_ICEBERG);
        else return biome.isRegistryKey(biomeKey);
    }

    @Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;sampleHeightmap(Lnet/minecraft/world/Heightmap$Type;II)I", ordinal = 1))
    private void reef$customSurfaceBuildersBefore(RandomState randomState, BiomeAccess biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, HeightContext context, Chunk chunk, ChunkNoiseSampler chunkNoiseSampler, SurfaceRules.MaterialRule surfaceRule, CallbackInfo ci,
                                                  @Local Holder<Biome> biome, @Local(ordinal = 4) int x, @Local(ordinal = 5) int z, @Local BlockColumn chunkBlockColumn) {
        CustomSurfaceBuilder.PRE_VANILLA.invoker().register(defaultBlock, seaLevel, biomeAccess, chunk, chunkBlockColumn, x, z);

        // Move this to kotlin in the future
        if (biome.isIn(ReefTags.HAS_ERODED_PILLAR)) {
            int y = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, x, z) + 1;
            double sn = this.badlandsSurfaceNoise.sample(x, 0.0, z);
            double pn = this.badlandsPillarNoise.sample(x * 0.2, 0.0, z * 0.2);
            double e = Math.min(Math.abs(sn * 8.25), pn * 15.0);
            if (!(e <= 0.0)) {
                double h = Math.abs(this.badlandsPillarRootNoise.sample((double) x * 0.75, 0.0, (double) z * 0.75) * 1.5);
                int j = MathHelper
                        .floor(64.0 + Math.min(e * e * 2.5, Math.ceil(h * 50.0) + 24.0)) - Math.max(seaLevel - y, 0);
                if (y <= j) {
                    for (int k = j; k >= chunk.getBottomY() && chunkBlockColumn.getState(k).isIn(BlockTags.REPLACEABLE); --k) {
                        chunkBlockColumn.setState(k, this.defaultBlock);
                    }
                }
            }
        }
    }

    @Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/surfacebuilder/SurfaceBuilder;buildFrozenOceanSpecificSurface(ILnet/minecraft/world/biome/Biome;Lnet/minecraft/world/gen/chunk/BlockColumn;Lnet/minecraft/util/math/BlockPos$Mutable;III)V", shift = At.Shift.BY, by = 2))
    private void reef$customSurfaceBuildersAfter(RandomState randomState, BiomeAccess biomeAccess, Registry<Biome> biomeRegistry, boolean useLegacyRandom, HeightContext context, Chunk chunk, ChunkNoiseSampler chunkNoiseSampler, SurfaceRules.MaterialRule surfaceRule,
                                                 CallbackInfo ci, @Local(ordinal = 4) int x, @Local(ordinal = 5) int z, @Local BlockColumn chunkBlockColumn) {
        CustomSurfaceBuilder.POST_VANILLA.invoker().register(defaultBlock, seaLevel, biomeAccess, chunk, chunkBlockColumn, x, z);

    }
}