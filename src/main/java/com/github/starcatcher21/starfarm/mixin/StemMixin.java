package com.github.starcatcher21.starfarm.mixin;

import com.github.starcatcher21.starfarm.data.CropGrowthData;
import com.github.starcatcher21.starfarm.data.GrowTime;
import com.github.starcatcher21.starfarm.mixinInterfaces.CropInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
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
        StemBlock crop = (StemBlock) (Object) this;

        if (level.getRawBrightness(pos, 0) < 9) {
            return;
        }

        int maxAge     = StemBlock.MAX_AGE;
        int currentAge = state.getValue(StemBlock.AGE);
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
            level.setBlock(pos, state.setValue(StemBlock.AGE, newAge), 2);
        }

        if (progress >= growTime || newAge >= maxAge) {
            data.clearProgress(key);
            Registry<Block> blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
            Optional<Block> fruit = blocks.getOptional(crop.fruit);
            if (fruit.isPresent()) {
                level.setBlock(pos, fruit.get().defaultBlockState(), 2);
            }
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
}
