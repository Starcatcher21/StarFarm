/*? if fabric || neoforge {*/
package com.github.starcatcher21.starfarm.datagen;

import com.github.starcatcher21.starfarm.StarFarm;
import com.github.starcatcher21.starfarm.data.GrowTime;
import com.github.starcatcher21.starfarm.datagen.provider.GrowTimeDataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class GrowTimeProvider extends GrowTimeDataProvider {
    public GrowTimeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final GrowTime WHEAT = new GrowTime(
            Blocks.WHEAT.defaultBlockState(),
            4
    );
    public static final GrowTime POTATOES = new GrowTime(
            Blocks.POTATOES.defaultBlockState(),
            6
    );
    public static final GrowTime CARROTS = new GrowTime(
            Blocks.CARROTS.defaultBlockState(),
            3
    );
    public static final GrowTime BEETROOTS = new GrowTime(
            Blocks.BEETROOTS.defaultBlockState(),
            6
    );
    public static final GrowTime MELON = new GrowTime(
            Blocks.MELON_STEM.defaultBlockState(),
            12
    );
    public static final GrowTime PUMPKIN = new GrowTime(
            Blocks.PUMPKIN_STEM.defaultBlockState(),
            13
    );
    public static final GrowTime TORCHFLOWER = new GrowTime(
            Blocks.TORCHFLOWER_CROP.defaultBlockState(),
            5
    );
    public static final GrowTime PITCHERPLANT = new GrowTime(
            Blocks.PITCHER_CROP.defaultBlockState(),
            10
    );

    @Override
    protected void addGrowTime(BiConsumer<Identifier, GrowTime> exporter, HolderLookup.Provider registries) {
        exporter.accept(
              Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "wheat"),
                WHEAT
        );
        exporter.accept(
                Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "potatoes"),
                POTATOES
        );
        exporter.accept(
                Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "carrots"),
                CARROTS
        );
        exporter.accept(
                Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "beetroots"),
                BEETROOTS
        );
        exporter.accept(
                Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "melon"),
                MELON
        );
        exporter.accept(
                Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "pumpkin"),
                PUMPKIN
        );
        exporter.accept(
                Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "torchflower"),
                TORCHFLOWER
        );
        exporter.accept(
                Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "pitcherplant"),
                PITCHERPLANT
        );
    }
}
//? }
