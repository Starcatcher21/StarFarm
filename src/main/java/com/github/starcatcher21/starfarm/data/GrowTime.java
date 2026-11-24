package com.github.starcatcher21.starfarm.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GrowTime {
    public static final Codec<GrowTime> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("plant").forGetter(GrowTime::getPlant),
            Codec.INT.fieldOf("growTime").forGetter(GrowTime::getGrowTime)
    ).apply(instance, GrowTime::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrowTime> PACKET_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    public static List<GrowTime> list = new ArrayList<>();

    public final BlockState PLANT;
    public final int GROWTIME;

    private BlockState getPlant() {
        return this.PLANT;
    }

    private int getGrowTime() {
        return this.GROWTIME;
    }

    public GrowTime(BlockState plant, int growTime) {
        PLANT = plant;
        GROWTIME = growTime;
        list.add(this);
    }

    public boolean gen(Level world, BlockPos pos) {
        return true;
    }
}
