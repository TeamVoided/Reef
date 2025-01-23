package org.teamvoided.reef.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.piece.PoolStructurePiece;
import net.minecraft.structure.piece.StructurePiece;
import net.minecraft.structure.piece.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.reef.Reef.log;

@Debug(export = true)
@Mixin(PoolStructurePiece.class)
public abstract class PoolStructurePieceMixin extends StructurePiece {
    @Unique
    private boolean reef$isSuccess = true;

    protected PoolStructurePieceMixin(StructurePieceType type, int generationDepth, BlockBox boundingBox) {
        super(type, generationDepth, boundingBox);
    }

    @ModifyExpressionValue(method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/random/RandomGenerator;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/pool/StructurePoolElement;generate(Lnet/minecraft/structure/StructureTemplateManager;Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/BlockRotation;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/random/RandomGenerator;Lnet/minecraft/world/gen/feature/LiquidSettings;Z)Z"))
    boolean captureSuccess(boolean original) {
        reef$isSuccess = original;
        return original;
    }

    @Inject(method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/random/RandomGenerator;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At("TAIL"))
    void placeAllNeeded(StructureWorldAccess world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomGenerator random, BlockBox boundingBox, BlockPos pos, boolean keepJigsaws, CallbackInfo ci) {
        if (keepJigsaws) {
            log.info("Keeping jigsaws");
            return;
        }
        if (!reef$isSuccess) {
            log.info("Jigsaw failed");
            return;
        }
        log.info("Jigsaw succeeded");


//        for (int i = -1; i < 2; i++) {
//            for (int j = -1; j < 2; j++) {
//                this.fillDownwards(world, world.getBlockState(pos.add(i, 0, j)), pos.getX() + i, pos.getY() - 1, pos.getZ() + j, boundingBox);
//            }
//        }
    }
}
