package com.github.starcatcher21.starfarm;

import com.github.starcatcher21.starfarm.blocks.Blocks;
import com.github.starcatcher21.starfarm.creative.ItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*? if fabric {*/
import net.fabricmc.api.ModInitializer;
/*?}*/


/*? if neoforge {*/
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
*//*?}*/

/*? if neoforge {*/
/*@Mod(StarFarm.MOD_ID)
*//*?}*/
public class StarFarm /*? if fabric {*/ implements ModInitializer /*?}*/ {
    public static final String MOD_ID = "starfarm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /*? if neoforge {*/
    /*public StarFarm(IEventBus modEventBus) {
        Blocks.init();
        ItemGroup.init();
    }
    *//*?}*/

    /*? if fabric {*/
    @Override
    public void onInitialize() {
        Blocks.init();
        ItemGroup.init();
    }
    /*?}*/
}
