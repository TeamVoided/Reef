package org.teamvoided.reef.mixin.structure;

import dev.worldgen.lithostitched.worldgen.processor.UnboundReferenceProcessor;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(UnboundReferenceProcessor.class)
public interface UnboundReferenceProcessorAccessor {

    @Invoker("<init>")
    static UnboundReferenceProcessor reef$new(ResourceLocation name) {
        throw new IllegalAccessError("Mixin Failed");
    }
}
