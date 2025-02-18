package com.gregtechceu.gtceu.core.mixins;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorage;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKey;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.core.MixinHelpers;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = ModelManager.class)
public abstract class ModelManagerMixin {
    @Inject(method = "reload", at = @At(value = "HEAD"))
    private void injectFluidModelTexture(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir) { // Have to use a redirect here cuz it's to constructor and mixin doesn't like that much
        for (Material material : GTRegistries.MATERIALS.values()) {
            MaterialIconSet iconSet = material.getMaterialIconSet();
            if (material.hasProperty(PropertyKey.FLUID)) {
                FluidProperty fluid = material.getProperty(PropertyKey.FLUID);

                for (FluidStorageKey key : FluidStorageKey.allKeys()) {
                    FluidStorage.FluidEntry fluidEntry = fluid.getStorage().getEntry(key);
                    if (fluidEntry != null) {
                        if (fluidEntry.getStillTexture() == null) {
                            ResourceLocation foundTexture = key.getIconType().getBlockTexturePath(iconSet, false);
                            fluidEntry.setStillTexture(foundTexture);
                        }
                        if (fluidEntry.getFlowTexture() == null) {
                            fluidEntry.setFlowTexture(fluidEntry.getStillTexture());
                        }
                        MixinHelpers.addFluidTexture(material, fluidEntry);
                    }
                }

            }
        }
    }
}
