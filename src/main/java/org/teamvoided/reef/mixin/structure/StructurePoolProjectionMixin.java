package org.teamvoided.reef.mixin.structure;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"rawtypes", "SameParameterValue"})
@Mixin(StructureTemplatePool.Projection.class)
public abstract class StructurePoolProjectionMixin {

//    TerrainAdjustmentClass ask about and see if that is what causes issues

    @Mutable
    @Shadow
    @Final
    private static StructureTemplatePool.Projection[] $VALUES;
    @Mutable
    @Shadow
    @Final
    public static StringRepresentable.EnumCodec<StructureTemplatePool.Projection> CODEC;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clInit(CallbackInfo ci) {
        register("SEAFLOOR_MATCHING", "seafloor_matching", ImmutableList.of(new GravityProcessor(Heightmap.Types.OCEAN_FLOOR_WG, -1)));
    }

    @Invoker("<init>")
    private static StructureTemplatePool.Projection invokeInit(String name, int id, String id2, ImmutableList processors) {
        throw new AssertionError();
    }

    @Unique
    private static void register(String name, String id, ImmutableList processors) {
        ArrayList<StructureTemplatePool.Projection> values = new ArrayList<>(Arrays.asList($VALUES));
        StructureTemplatePool.Projection type = invokeInit(name, values.get(values.size() - 1).ordinal() + 1, id, processors);
        values.add(type);
        $VALUES = values.toArray(new StructureTemplatePool.Projection[]{});
        CODEC = StringRepresentable.fromEnum(() -> $VALUES);
    }
}