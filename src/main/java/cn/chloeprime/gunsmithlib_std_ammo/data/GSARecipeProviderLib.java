package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.DatagenRegistryHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems.WAFER;

public abstract class GSARecipeProviderLib extends RecipeProvider implements DatagenRegistryHelper {
    public GSARecipeProviderLib(PackOutput pOutput) {
        super(pOutput);
    }

    @SuppressWarnings("SameParameterValue")
    protected void gsaNineBlockStorageRecipes(Consumer<FinishedRecipe> output, RecipeCategory unpackedCategory, ItemLike unpackedCore, TagKey<Item> unpackedIngredient, RecipeCategory packedCategory, ItemLike packed) {
        var unpackedId = getKey(unpackedCore.asItem());
        var packedId = getKey(packed.asItem());
        var unpackRecipeId = "%s:%s_from_%s".formatted(unpackedId.getNamespace(), unpackedId.getPath(), packedId.getPath());
        var packRecipeId = "%s:%s_from_%s".formatted(packedId.getNamespace(), packedId.getPath(), unpackedId.getPath());
        gsaNineBlockStorageRecipes(output, unpackedCategory, unpackedCore, unpackedIngredient, packedCategory, packed, packRecipeId, null, unpackRecipeId, null);
    }

    @SuppressWarnings("SameParameterValue")
    protected void gsaNineBlockStorageRecipes(Consumer<FinishedRecipe> output, RecipeCategory unpackedCategory, ItemLike unpackedCore, TagKey<Item> unpackedIngredient, RecipeCategory packedCategory, ItemLike packed, String packedName, @Nullable String packedGroup, String unpackedName, @Nullable String unpackedGroup) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpackedCore, 9)
                .requires(packed)
                .group(unpackedGroup)
                .unlockedBy(getHasName(packed), has(packed))
                .save(output, new ResourceLocation(unpackedName));
        ShapedRecipeBuilder.shaped(packedCategory, packed)
                .define('C', unpackedCore)
                .define('#', unpackedIngredient)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .group(packedGroup)
                .unlockedBy(getHasName(unpackedCore), has(unpackedCore))
                .save(output, new ResourceLocation(packedName));
    }

    @SuppressWarnings("SameParameterValue")
    @ParametersAreNonnullByDefault
    protected void gsaOreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<? extends ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        gsaOreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    @SuppressWarnings("SameParameterValue")
    @ParametersAreNonnullByDefault
    protected void gsaOreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<? extends ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        gsaOreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    @ParametersAreNonnullByDefault
    protected void gsaOreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<? extends ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, GunsmithLibStdAmmoMod.loc(getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike)));
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void gsaStonecutterResultFromBase(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(pMaterial), pCategory, pResult, pResultCount).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer, GunsmithLibStdAmmoMod.loc(getConversionRecipeName(pResult, pMaterial) + "_stonecutting"));
    }

    protected void photolithography(
            Consumer<FinishedRecipe> output,
            Supplier<? extends ItemLike> result,
            TagKey<Item> top,
            TagKey<Item> middle
    ) {
        ItemLike resultItem = result.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, resultItem)
                .define('C', top)
                .define('D', middle)
                .define('S', WAFER.get())
                .pattern("C")
                .pattern("D")
                .pattern("S")
                .unlockedBy("has_wafer", has(WAFER.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, resultItem, 3)
                .define('C', top)
                .define('D', middle)
                .define('S', WAFER.get())
                .pattern("CCC")
                .pattern("DDD")
                .pattern("SSS")
                .unlockedBy("has_wafer", has(WAFER.get()))
                .save(output, GunsmithLibStdAmmoMod.loc(getItemName(resultItem) + "_batched"));
    }
}
