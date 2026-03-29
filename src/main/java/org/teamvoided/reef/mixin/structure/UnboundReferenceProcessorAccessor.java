package org.teamvoided.reef.mixin.structure;

import dev.worldgen.lithostitched.worldgen.processor.UnboundReferenceProcessor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(UnboundReferenceProcessor.class)
public interface UnboundReferenceProcessorAccessor {

    @Invoker("<init>")
    static UnboundReferenceProcessor reef$new(ResourceKey<StructureProcessorList> key) {
        throw new IllegalAccessError("Mixin Failed");
    }

}