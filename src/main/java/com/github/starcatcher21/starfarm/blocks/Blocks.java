package com.github.starcatcher21.starfarm.blocks;

import com.github.starcatcher21.starfarm.StarFarm;
import com.github.starcatcher21.starfarm.blocks.types.Sprinkler;
import com.github.starcatcher21.starlib.blockitems.BlockRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(StarFarm.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(StarFarm.MOD_ID, Registries.ITEM);
    public static final BlockRegistry BLOCK_REGISTRY = new BlockRegistry(StarFarm.MOD_ID, BLOCKS, ITEMS);

    public static final RegistrySupplier<Block> SPRINKLER = BLOCK_REGISTRY.register("sprinkler", Sprinkler::new, BlockBehaviour.Properties.of()
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()
            .strength(2.0f)
    );
    public static final RegistrySupplier<Block> GOLD_SPRINKLER = BLOCK_REGISTRY.register("gold_sprinkler", Sprinkler::new, BlockBehaviour.Properties.of()
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()
            .strength(2.0f)
    );
    public static final RegistrySupplier<Block> DIAMOND_SPRINKLER = BLOCK_REGISTRY.register("diamond_sprinkler", Sprinkler::new, BlockBehaviour.Properties.of()
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()
            .strength(2.0f)
    );
    public static final RegistrySupplier<Block> NETHERITE_SPRINKLER = BLOCK_REGISTRY.register("netherite_sprinkler", Sprinkler::new, BlockBehaviour.Properties.of()
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()
            .strength(2.0f)
    );

    public static void init() {
        BLOCKS.register();
        ITEMS.register();
    }

    
}
