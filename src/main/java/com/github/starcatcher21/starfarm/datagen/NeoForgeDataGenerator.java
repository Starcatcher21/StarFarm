package com.github.starcatcher21.starfarm.datagen;//? if neoforge {

import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class NeoForgeDataGenerator {
    private NeoForgeDataGenerator() {}

    public static void gatherServerData(GatherDataEvent.Server event) {
        gatherData(event);
    }

    public static void gatherClientData(GatherDataEvent.Client event) {
    }

    public static void gatherData(GatherDataEvent event) {
        //? if >= 26.3 {
        /*if (event instanceof GatherDataEvent.Server) {
            event.addProvider(new GrowTimeProvider(
                    event.getGenerator().getPackOutput(),
                    event.getReloadableLookupProvider()
            ));
        }
        *///? } else {
        if (event instanceof GatherDataEvent.Server) {
            event.addProvider(new GrowTimeProvider(
                    event.getGenerator().getPackOutput(),
                    event.getLookupProvider()
            ));
        }
        //? }
    }
}
//?}
