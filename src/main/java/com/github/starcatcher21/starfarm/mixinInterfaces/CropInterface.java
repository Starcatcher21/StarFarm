package com.github.starcatcher21.starfarm.mixinInterfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface CropInterface {
    void starfarm$daily(BlockState state, ServerLevel level, BlockPos pos);
    int starfarm$growTime(BlockState state, ServerLevel level, BlockPos pos);
}
