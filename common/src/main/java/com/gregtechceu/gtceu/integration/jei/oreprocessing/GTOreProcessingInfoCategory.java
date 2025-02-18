package com.gregtechceu.gtceu.integration.jei.oreprocessing;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.lowdragmc.lowdraglib.jei.ModularUIRecipeCategory;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey.DUST;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey.ORE;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Aluminium;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Iron;

public class GTOreProcessingInfoCategory extends ModularUIRecipeCategory<GTOreProcessingInfoWrapper> {
    public final static RecipeType<GTOreProcessingInfoWrapper> RECIPE_TYPE = new RecipeType<>(GTCEu.id("ore_processing_diagram"), GTOreProcessingInfoWrapper.class);
    private final IDrawable background;
    private final IDrawable icon;

    public GTOreProcessingInfoCategory(IJeiHelpers helpers) {
        IGuiHelper guiHelper = helpers.getGuiHelper();
        this.background = guiHelper.createBlankDrawable(186, 174);
        this.icon = helpers.getGuiHelper().createDrawableItemStack(ChemicalHelper.get(ore,Iron));
    }

    public static void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(RECIPE_TYPE, GTRegistries.MATERIALS.values().stream()
                .filter((material) -> material.hasProperty(PropertyKey.ORE))
                .map(GTOreProcessingInfoWrapper::new)
                .toList());
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (Material mat : GTRegistries.MATERIALS) {
            if (mat.hasProperty(ORE)) {
                registration.addRecipeCatalyst(ChemicalHelper.get(ore, mat), RECIPE_TYPE);
                registration.addRecipeCatalyst(ChemicalHelper.get(rawOre, mat), RECIPE_TYPE);
                registration.addRecipeCatalyst(ChemicalHelper.get(crushed, mat), RECIPE_TYPE);
                registration.addRecipeCatalyst(ChemicalHelper.get(crushedPurified, mat), RECIPE_TYPE);
                registration.addRecipeCatalyst(ChemicalHelper.get(crushedRefined, mat), RECIPE_TYPE);
                registration.addRecipeCatalyst(ChemicalHelper.get(ore, mat), RECIPE_TYPE);
                if (mat.hasProperty(DUST)) {
                    registration.addRecipeCatalyst(ChemicalHelper.get(dust, mat), RECIPE_TYPE);
                }
            }
        }
    }


    @Override
    @Nonnull
    public RecipeType<GTOreProcessingInfoWrapper> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("gtceu.jei.ore_processing_info");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

}
