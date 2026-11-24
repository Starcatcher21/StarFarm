package com.github.starcatcher21.starfarm.mixin;

import com.github.starcatcher21.starfarm.data.CropGrowthData;
import com.github.starcatcher21.starfarm.data.GrowTime;
import com.github.starcatcher21.starfarm.mixinInterfaces.CropInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.PitcherCropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.PitcherCropBlock.AGE;
import static net.minecraft.world.level.block.PitcherCropBlock.HALF;

@Mixin(PitcherCropBlock.class)
public abstract class PitcherplantMixin implements CropInterface {

    @Shadow
    protected abstract void grow(ServerLevel level, BlockState lowerState, BlockPos lowerPos, int increase);

    @Shadow
    private static boolean isDouble(int age) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    public void starfarm$isRandomlyTicking(final BlockState state, final ServerLevel level,
                                           final BlockPos pos, final RandomSource random,
                                           CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public void starfarm$daily(BlockState state, ServerLevel level, BlockPos pos) {
        PitcherCropBlock crop = (PitcherCropBlock) (Object) this;

        if (level.getRawBrightness(pos, 0) < 9) {
            return;
        }

        int maxAge     = PitcherCropBlock.MAX_AGE;
        int currentAge = state.getValue(AGE);
        if (currentAge >= maxAge) {
            return;
        }

        int growTime = this.starfarm$growTime(state, level, pos);
        if (growTime <= 0) {
            return;
        }

        CropGrowthData data = level.getDataStorage()
                .computeIfAbsent(CropGrowthData.TYPE);
        long key = pos.asLong();

        Integer stored = data.getProgress(key);
        int progress;
        if (currentAge == 0) {
            progress = stored != null ? stored : 0;
        } else {
            int fromAge = (currentAge * growTime) / Math.max(1, maxAge);
            progress = stored != null ? Math.max(stored, fromAge) : fromAge;
        }

        progress++;
        int newAge = Math.min(maxAge, (progress * maxAge) / growTime);

        if (newAge > currentAge) {
            starfarm$grow(level, state, pos, 1);
        }

        if (progress >= growTime || newAge >= maxAge) {
            data.clearProgress(key);
        } else {
            data.setProgress(key, progress);
        }
    }

    @Override
    public int starfarm$growTime(BlockState state, ServerLevel level, BlockPos pos) {
        for (GrowTime gt : GrowTime.list) {
            if (state.getBlock().equals(gt.PLANT.getBlock())) {
                return gt.GROWTIME;
            }
        }
        return 7;
    }

    @Unique
    private void starfarm$grow(ServerLevel level, BlockState lowerState, BlockPos lowerPos, int increase) {
        int updatedAge = Math.min((Integer)lowerState.getValue(AGE) + increase, 4);
        if (starfarm$canGrow(level, lowerPos, lowerState, updatedAge)) {
            BlockState newLowerState = (BlockState)lowerState.setValue(AGE, updatedAge);
            level.setBlock(lowerPos, newLowerState, 2);
            if (isDouble(updatedAge)) {
                level.setBlockAndUpdate(lowerPos.above(), (BlockState)newLowerState.setValue(HALF, DoubleBlockHalf.UPPER));
            }
        }
    }

    @Unique
    private static boolean starfarm$canGrowInto(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.is(Blocks.PITCHER_CROP);
    }

    @Unique
    private static boolean starfarm$sufficientLight(LevelReader level, BlockPos pos) {
        return CropBlock.hasSufficientLight(level, pos);
    }

    @Unique
    private boolean starfarm$canGrow(LevelReader level, BlockPos lowerPos, BlockState lowerState, int newAge) {
        return !isMaxAge(lowerState) && starfarm$sufficientLight(level, lowerPos) && level.isInsideBuildHeight(lowerPos.above()) && (!isDouble(newAge) || starfarm$canGrowInto(level, lowerPos.above()));
    }

    @Unique
    private boolean isMaxAge(BlockState state) {
        return (Integer)state.getValue(AGE) >= 4;
    }

}
