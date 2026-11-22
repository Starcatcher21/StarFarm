package com.github.starcatcher21.starfarm.mixin;

import com.github.starcatcher21.starfarm.blocks.Blocks;
import com.github.starcatcher21.starfarm.mixinInterfaces.CropInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandMixin {

    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    private void starfarm$randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random, CallbackInfo ci) {
        level.scheduleTick(pos, (FarmlandBlock) (Object) this, 1);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void starfarm$tick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random, CallbackInfo ci) {
        level.scheduleTick(pos, (FarmlandBlock) (Object) this, 1);
        long timeOfDay = level.getDefaultClockTime() % 24000;
        if (timeOfDay == 0) {
            int moisture = (Integer)state.getValue(FarmlandBlock.MOISTURE);
            if (!starfarm$isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
                if (moisture > 0) {
                    level.setBlock(pos, (BlockState)state.setValue(FarmlandBlock.MOISTURE, 0), 2);
                } else if (!starfarm$shouldMaintainFarmland(level, pos)) {
                    //? if < 26.3 {
                    /*((FarmlandBlock) (Object)this).turnToDirt((Entity) null, state, level, pos);
                    *///? } else {
                    ((FarmlandBlock) (Object)this).turnToBaseBlock((Entity)null, state, level, pos);
                    //? }
                }
            } else if (moisture < 7) {
                level.setBlock(pos, (BlockState)state.setValue(FarmlandBlock.MOISTURE, 7), 2);
            } else if (moisture == 7) {
                var block = level.getBlockState(pos.above());
                if (block.getBlock() instanceof StemBlock sb) {
                    CropInterface cropinterface = (CropInterface) sb;
                    cropinterface.starfarm$daily(block, level, pos.above());
                }
                if (block.getBlock() instanceof CropBlock cb) {
                    CropInterface cropinterface = (CropInterface) cb;
                    cropinterface.starfarm$daily(block, level, pos.above());
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "fallOn", cancellable = true)
    private void starfarm$fallOn(CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    private static boolean starfarm$isNearWater(final LevelReader level, final BlockPos pos) {
        var cross = List.of(pos.offset(-1, 0, 0), pos.offset(1, 0, 0), pos.offset(0, 0, -1), pos.offset(0, 0, 1));
        for (BlockPos blockPos : cross) {
            if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                return true;
            }
        }
        for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, 1, -1), pos.offset(1, 1, 1))) {
            if (level.getBlockState(blockPos).getBlock().equals(Blocks.SPRINKLER.get())) {
                return true;
            }
        }
        for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-2, 1, -2), pos.offset(2, 1, 2))) {
            if (level.getBlockState(blockPos).getBlock().equals(Blocks.GOLD_SPRINKLER.get())) {
                return true;
            }
        }
        for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-3, 1, -3), pos.offset(3, 1, 3))) {
            if (level.getBlockState(blockPos).getBlock().equals(Blocks.DIAMOND_SPRINKLER.get())) {
                return true;
            }
        }
        for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, 1, -4), pos.offset(4, 1, 4))) {
            if (level.getBlockState(blockPos).getBlock().equals(Blocks.NETHERITE_SPRINKLER.get())) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private static boolean starfarm$shouldMaintainFarmland(final BlockGetter level, final BlockPos pos) {
        return level.getBlockState(pos.above()).is(BlockTags.MAINTAINS_FARMLAND);
    }
}
