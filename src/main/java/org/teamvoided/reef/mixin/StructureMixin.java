package org.teamvoided.reef.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Debug(export = true)
@Mixin(Structure.class)
public class StructureMixin {
    public HashMap<BlockPos, BlockState> placedBlocks = new HashMap<>();

    @Inject(method = "place", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    void addPillars(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, RandomGenerator random, int flags, CallbackInfoReturnable<Boolean> cir,
                    @Local(ordinal = 2) BlockPos blockPos, @Local(ordinal = 0) BlockState state) {
        if (state.isAir() || state.hasBlockEntity()) return;
        placedBlocks.put(blockPos, state);
    }

    @Inject(method = "place", at = @At(value = "INVOKE", ordinal = 3, target = "Ljava/util/List;isEmpty()Z"))
    void addPillars(ServerWorldAccess world, BlockPos posI, BlockPos pivot, StructurePlacementData placementData, RandomGenerator random, int flags, CallbackInfoReturnable<Boolean> cir,
                    @Local(ordinal = 2) int y) {
        placedBlocks.forEach((pos, state) -> {
            if (pos.getY() <= y && state.isFullCube(world, pos)) this.fillDownwards(world, state, pos.getX(), pos.getY() - 1, pos.getZ());
        });

    }

    @Unique
    protected void fillDownwards(ServerWorldAccess world, BlockState state, int x, int y, int z) {
        BlockPos.Mutable mutable = new BlockPos(x, y, z).mutableCopy();
        while (canReplace(world.getBlockState(mutable)) && mutable.getY() > world.getBottomY() + 1) {
            world.setBlockState(mutable, state, Block.NOTIFY_LISTENERS);
            mutable.move(Direction.DOWN);
        }
    }

    public boolean canReplace(BlockState state) {
        return state.isAir() || state.isLiquid() || state == Blocks.GLASS.getDefaultState();
    }

}
