package com.github.starcatcher21.starfarm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void starfarm$onInit(CallbackInfo ci) {

    }

    @Inject(at = @At("HEAD"), method = "onPlace")
    private void starfarm$onPlace(BlockState state, Level level, BlockPos pos,
                                  BlockState oldState, boolean movedByPiston,
                                  CallbackInfo ci) {
        if (level instanceof ServerLevel serverLevel && state.getBlock() instanceof FarmlandBlock farmlandBlock) {
            serverLevel.scheduleTick(pos, farmlandBlock, 1);
        }
    }
}
