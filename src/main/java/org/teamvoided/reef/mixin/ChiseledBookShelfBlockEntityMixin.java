package org.teamvoided.reef.mixin;

import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.RandomizableContainer.LOOT_TABLE_SEED_TAG;
import static net.minecraft.world.RandomizableContainer.LOOT_TABLE_TAG;
import static org.teamvoided.reef.util.mixin.MixinsKt.fillBookshelfFromLootTable;

@Mixin(ChiseledBookShelfBlockEntity.class)
public class ChiseledBookShelfBlockEntityMixin {

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    void fillBooksFromTable(ValueInput valueInput, CallbackInfo ci) {
        var table = valueInput.read(LOOT_TABLE_TAG, LootTable.KEY_CODEC).orElse(null);
        if (table == null) return;
        fillBookshelfFromLootTable((ChiseledBookShelfBlockEntity) (Object) this, table, valueInput.getLongOr(LOOT_TABLE_SEED_TAG, 0L));
    }

}