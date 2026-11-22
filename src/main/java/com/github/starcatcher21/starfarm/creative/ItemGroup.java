package com.github.starcatcher21.starfarm.creative;

import com.github.starcatcher21.starfarm.StarFarm;
import com.github.starcatcher21.starfarm.blocks.Blocks;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemGroup {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(StarFarm.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> STARFARM_GROUP = TABS.register(Identifier.fromNamespaceAndPath(StarFarm.MOD_ID, "starfarm_group"), () -> CreativeTabRegistry.create(
            builder -> builder
                    .icon(() -> new ItemStack(Items.WHEAT))
                    .title(Component.translatable("itemGroup.Starfarm"))
                    .displayItems((parameters, output) -> {
                        output.accept(Blocks.SPRINKLER.get());
                        output.accept(Blocks.GOLD_SPRINKLER.get());
                        output.accept(Blocks.DIAMOND_SPRINKLER.get());
                        output.accept(Blocks.NETHERITE_SPRINKLER.get());
                    })
    ));

    public static void init() {
        TABS.register();
    }
}
