package org.teamvoided.reef.mixin.structure;

import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.teamvoided.reef.util.mixin.StructureRefHolder;

@Mixin(TemplateStructurePiece.class)
public interface TemplateStructurePieceAccessor extends StructureRefHolder {

    @Accessor("placeSettings")
    StructurePlaceSettings reef$getPlaceSettings();

    @Accessor("placeSettings")
    void reef$setPlaceSettings(StructurePlaceSettings placeSettings);

}
