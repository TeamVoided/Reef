package org.teamvoided.reef.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.GravityStructureProcessor;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"deprecation", "rawtypes", "SameParameterValue"})
@Mixin(StructurePool.Projection.class)
public class StructurePoolProjectionMixin {

//    TerrainAdjustmentClass ask about and see if that is what causes issues

    @Mutable
    @Shadow
    @Final
    private static StructurePool.Projection[] field_16683;

    @Mutable
    @Shadow
    @Final

    public static StringIdentifiable.EnumCodec<StructurePool.Projection> CODEC;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clInit(CallbackInfo ci) {
        register("SEAFLOOR_MATCHING", "seafloor_matching", ImmutableList.of(new GravityStructureProcessor(Heightmap.Type.OCEAN_FLOOR_WG, -1)));
    }

    @Invoker("<init>")
    private static StructurePool.Projection invokeInit(String name, int id, String id2, ImmutableList processors) {
        throw new AssertionError();
    }

    @Unique
    private static void register(String name, String id, ImmutableList processors) {
        ArrayList<StructurePool.Projection> values = new ArrayList<>(Arrays.asList(field_16683));
        StructurePool.Projection type = invokeInit(name, values.get(values.size() - 1).ordinal() + 1, id, processors);
        values.add(type);
        field_16683 = values.toArray(new StructurePool.Projection[]{});
        CODEC = StringIdentifiable.createEnumCodec(() -> field_16683);
    }
}