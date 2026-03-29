package org.teamvoided.reef.mixin.structure;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.worldgen.lithostitched.worldgen.processor.ReferenceStructureProcessor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.reef.data.ReefStructureTags;


@Mixin(Structure.class)
public abstract class StructureMixin {

    @ModifyExpressionValue(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePiecesBuilder;build()Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;"))
    PiecesContainer modifyPieces(PiecesContainer original, Holder<Structure> holder, @Local(argsOnly = true) RegistryAccess registryAccess) {
        if (holder.is(ReefStructureTags.HAS_INJECTED_PROCESSOR_LISTS)) {
            var structure = holder.unwrapKey().orElse(null);
            if (structure != null) {
                for (StructurePiece piece : original.pieces()) {
                    if (!(piece instanceof TemplateStructurePieceAccessor pc)) {
                        continue;
                    }
                    pc.reef_setStructureRef(structure.identifier());
                    pc.reef$setPlaceSettings(pc.reef$getPlaceSettings().addProcessor(reef$addRSP(registryAccess, structure.identifier())));
                }
            }
        }
        return original;
    }

    @Unique
    public ReferenceStructureProcessor reef$addRSP(RegistryAccess registryAccess, Identifier name) {
        var processList = registryAccess.lookupOrThrow(Registries.PROCESSOR_LIST).get(ResourceKey.create(Registries.PROCESSOR_LIST, name));
        return new ReferenceStructureProcessor(processList.isPresent() ? HolderSet.direct(processList.get()) : HolderSet.empty());
    }

}