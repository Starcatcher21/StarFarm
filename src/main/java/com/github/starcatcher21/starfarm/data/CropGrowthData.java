package com.github.starcatcher21.starfarm.data;

import com.github.starcatcher21.starfarm.StarFarm;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CropGrowthData extends SavedData {
    private static final Codec<CropGrowthData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, Codec.INT)
                            .fieldOf("progress")
                            .forGetter(data -> data.progress.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            e -> Long.toString(e.getKey()),
                                            Map.Entry::getValue
                                    )))
            ).apply(instance, map -> {
                CropGrowthData data = new CropGrowthData();
                map.forEach((k, v) -> data.progress.put(Long.parseLong(k), v));
                return data;
            })
    );

    public static final SavedDataType<CropGrowthData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "starfarm_crop_growth"),
            CropGrowthData::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    private final Map<Long, Integer> progress = new HashMap<>();

    public Integer getProgress(long pos) {
        return progress.get(pos);
    }

    public void setProgress(long pos, int value) {
        progress.put(pos, value);
        setDirty();
    }

    public void clearProgress(long pos) {
        if (progress.remove(pos) != null) {
            setDirty();
        }
    }
}
