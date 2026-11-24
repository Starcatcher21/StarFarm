package com.github.starcatcher21.starfarm;

import com.github.starcatcher21.starfarm.blocks.Blocks;
import com.github.starcatcher21.starfarm.creative.ItemGroup;
import com.github.starcatcher21.starfarm.data.GrowTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*? if fabric {*/
/*import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
*//*?}*/


/*? if neoforge {*/
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
/*? if >=26.3 {*/
/*import net.neoforged.neoforge.registries.NewDatapackRegistryEvent;
*//*?} else {*/
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
/*?}*/
import com.github.starcatcher21.starfarm.datagen.NeoForgeDataGenerator;
/*?}*/

/*? if neoforge {*/
@Mod(StarFarm.MOD_ID)
/*?}*/
public class StarFarm /*? if fabric {*/ /*implements ModInitializer *//*?}*/ {
    public static final String MOD_ID = "starfarm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /*? if neoforge {*/
    public StarFarm(IEventBus modEventBus) {
        modEventBus.addListener(
                net.neoforged.neoforge.data.event.GatherDataEvent.Server.class,
                NeoForgeDataGenerator::gatherServerData
        );
        modEventBus.addListener(
                net.neoforged.neoforge.data.event.GatherDataEvent.Client.class,
                NeoForgeDataGenerator::gatherClientData
        );
        Blocks.init();
        ItemGroup.init();
        modEventBus.addListener(StarFarm::registerDatapackRegistries);
    }

    private static void registerDatapackRegistries(
            //? if >=26.3 {
            /*NewDatapackRegistryEvent
            *///?} else {
            DataPackRegistryEvent.NewRegistry
            //? }
                    event) {
        //? if >= 26.3 {
        /*event.worldRegistry(RegistryKeys.GROWTIME,     GrowTime.CODEC);
        *///? } else {
        event.dataPackRegistry(RegistryKeys.GROWTIME,     GrowTime.CODEC);
        //? }
    }
    /*?}*/

    /*? if fabric {*/
    /*@Override
    public void onInitialize() {
        Blocks.init();
        ItemGroup.init();
        DynamicRegistries.register(RegistryKeys.GROWTIME,     GrowTime.CODEC);
    }
    *//*?}*/
}
