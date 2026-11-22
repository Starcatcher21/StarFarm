package com.github.starcatcher21.starfarm.mixin;

import com.github.starcatcher21.starfarm.mixinInterfaces.CropInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(StemBlock.class)
public class StemMixin implements CropInterface {
    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    public void starfarm$isRandomlyTicking(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random, CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public void starfarm$daily(BlockState state, ServerLevel level, BlockPos pos) {
        var tis = (StemBlock) (Object) this;
        var random = level.getRandom();
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = state.getValue(StemBlock.AGE);
            if (age < StemBlock.MAX_AGE) {
                level.setBlock(pos, state.setValue(StemBlock.AGE, age + 1), 2);
            } else {
                Registry<Block> blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
                Optional<Block> fruit = blocks.getOptional(tis.fruit);
                Optional<Block> stem = blocks.getOptional(tis.attachedStem);
                if (fruit.isPresent() && stem.isPresent()) {
                    level.setBlockAndUpdate(pos, ((Block)fruit.get()).defaultBlockState());
                }
            }
        }
    }
}
