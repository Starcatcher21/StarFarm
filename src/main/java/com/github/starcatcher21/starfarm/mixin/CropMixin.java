package com.github.starcatcher21.starfarm.mixin;

import com.github.starcatcher21.starfarm.data.CropGrowthData;
import com.github.starcatcher21.starfarm.mixinInterfaces.CropInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public class CropMixin implements CropInterface {

    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    public void starfarm$isRandomlyTicking(final BlockState state, final ServerLevel level,
                                           final BlockPos pos, final RandomSource random,
                                           CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public void starfarm$daily(BlockState state, ServerLevel level, BlockPos pos) {
        CropBlock crop = (CropBlock) (Object) this;

        if (level.getRawBrightness(pos, 0) < 9) {
            return;
        }

        int maxAge     = crop.getMaxAge();
        int currentAge = crop.getAge(state);
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
            level.setBlock(pos, crop.getStateForAge(newAge), 2);
        }

        if (progress >= growTime || newAge >= maxAge) {
            data.clearProgress(key);
        } else {
            data.setProgress(key, progress);
        }
    }

    @Override
    public int starfarm$growTime(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.getBlock().equals(Blocks.WHEAT))     return 4;
        if (state.getBlock().equals(Blocks.POTATOES))  return 6;
        if (state.getBlock().equals(Blocks.CARROTS))   return 3;
        if (state.getBlock().equals(Blocks.BEETROOTS)) return 6;
        return 7;
    }
}
