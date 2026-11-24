package com.github.starcatcher21.starfarm.datagen.provider;

import com.github.starcatcher21.starfarm.data.GrowTime;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class GrowTimeDataProvider implements DataProvider {

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public GrowTimeDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.output = output;
        this.registriesFuture = registriesFuture;
    }

    protected abstract void addGrowTime(BiConsumer<Identifier, GrowTime> exporter, HolderLookup.Provider registries);

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return this.registriesFuture.thenCompose(registries -> {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            var pathResolver = this.output.createPathProvider(
                    PackOutput.Target.DATA_PACK,
                    "starfarm/growtime"
            );

            addGrowTime((id, growTime) -> {
                Path path = pathResolver.json(id);
                futures.add(DataProvider.saveStable(writer, registries, GrowTime.CODEC, growTime, path));
            }, registries);

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    @Override
    public String getName() {
        return "Starfarm GrowTime Generator";
    }
}
