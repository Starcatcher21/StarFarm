package com.github.starcatcher21.starfarm.blocks.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Sprinkler extends Block {
    public Sprinkler(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        long time = level.getDefaultClockTime() % 24000;
        if (time == 0) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ(), 0, 0, -0.5);
                level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX(), pos.getY() + 0.5, pos.getZ() + 0.5, -0.5, 0, 0);
                level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 1.0, 0, 0, 0.5);
                level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 1.0, pos.getY() + 0.5, pos.getZ() + 0.5, 0.5, 0, 0);
            }
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.4375, 0, 0.4375, 0.5625, 0.625, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.5, 0, 0.5625, 0.625, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.5, 0.5625, 0.5625, 0.625, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.5, 0.4375, 0.4375, 0.625, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5625, 0.5, 0.4375, 1, 0.625, 0.5625), BooleanOp.OR);

        return shape;
    }


}
