package com.github.starcatcher21.starfarm.mixin;

import com.github.starcatcher21.starfarm.mixinInterfaces.CropInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public class CropMixin implements CropInterface {
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    public void starfarm$isRandomlyTicking(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random, CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public void starfarm$daily(BlockState state, ServerLevel level, BlockPos pos) {
        var tis = (CropBlock) (Object) this;
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = tis.getAge(state);
            if (age < tis.getMaxAge()) {
                level.setBlock(pos, tis.getStateForAge(age + 1), 2);
            }
        }
    }
}
