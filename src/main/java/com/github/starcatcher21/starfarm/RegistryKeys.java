package com.github.starcatcher21.starfarm;

import com.github.starcatcher21.starfarm.data.GrowTime;
import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class RegistryKeys {
    public static final ResourceKey<Registry<GrowTime>> GROWTIME =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "growtime"));

    public static void init() {
    }
}
