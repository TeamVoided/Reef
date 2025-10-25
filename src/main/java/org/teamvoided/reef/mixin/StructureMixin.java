package org.teamvoided.reef.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.reef.data.ReefStructureTags;


@Mixin(Structure.class)
public abstract class StructureMixin {

    @ModifyExpressionValue(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePiecesBuilder;build()Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;"))
    PiecesContainer modifyPieces(PiecesContainer original, Holder<Structure> holder) {
        if (holder.is(ReefStructureTags.HAS_INJECTED_PROCESSOR_LISTS)) {
            var structure = holder.unwrapKey().get().location();
            for (StructurePiece piece : original.pieces()) {
                if (!(piece instanceof TemplateStructurePieceAccessor pc)) continue;
                pc.reef$setPlaceSettings(pc.reef$getPlaceSettings()
                        .addProcessor(UnboundReferenceProcessorAccessor.reef$new(structure)));
            }
        }
        return original;
    }
}
