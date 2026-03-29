package org.teamvoided.reef.mixin.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.reef.util.mixin.StructureRefHolder;

import java.util.function.Function;

import static org.teamvoided.reef.util.mixin.MixinsKt.FAILED_PARSE;
import static org.teamvoided.reef.util.mixin.MixinsKt.STRUCTURE_REF_KEY;

@Mixin(TemplateStructurePiece.class)
public class TemplateStructurePieceMixin implements StructureRefHolder {

    @Shadow
    protected StructurePlaceSettings placeSettings;

    @Unique
    private Identifier reef$structureRef = null;

    @Inject(method = "<init>(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceType;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Ljava/util/function/Function;)V", at = @At("TAIL"))
    void loadRefFromTag(StructurePieceType structurePieceType, CompoundTag nbt, StructureTemplateManager structureTemplateManager, Function<Identifier, StructurePlaceSettings> function, CallbackInfo ci) {
        if (!nbt.contains(STRUCTURE_REF_KEY)) return;
        var id = Identifier.tryParse(nbt.getStringOr(STRUCTURE_REF_KEY, FAILED_PARSE.toString()));
        if (id != null && id != FAILED_PARSE) {
            reef$structureRef = id;
            placeSettings.addProcessor(UnboundReferenceProcessorAccessor.reef$new(ResourceKey.create(Registries.PROCESSOR_LIST, reef$structureRef)));
        }
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    void structureRefSave(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag, CallbackInfo ci) {
        if (reef$structureRef != null) {
            compoundTag.putString(STRUCTURE_REF_KEY, reef$structureRef.toString());
        }
    }

    @Override
    public void reef_setStructureRef(@NotNull Identifier id) {
        reef$structureRef = id;
    }

    @Override
    public Identifier reef_getStructureRef() {
        return reef$structureRef;
    }

}