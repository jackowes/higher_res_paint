package xerca.xercamod.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class RecipeConditionSmelting extends SmeltingRecipe {
    private final Supplier<Boolean> condition;
    private RecipeSerializer serializer;

    public RecipeConditionSmelting(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn, Supplier<Boolean> condition) {
        super(idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
        this.condition = condition;
    }

    public RecipeConditionSmelting(SmeltingRecipe furnaceRecipe, Supplier<Boolean> condition, RecipeSerializer serializer){
        super(furnaceRecipe.getId(), furnaceRecipe.getGroup(), furnaceRecipe.getIngredients().get(0), furnaceRecipe.getResultItem(), furnaceRecipe.getExperience(), furnaceRecipe.getCookingTime());
        this.condition = condition;
        this.serializer = serializer;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(Container inv, Level worldIn) {
        if(!condition.get()){
            return false;
        }
        return super.matches(inv, worldIn);
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack assemble(Container inv) {
        if(!condition.get()){
            return ItemStack.EMPTY;
        }
        return super.assemble(inv);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    public void setSerializer(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeConditionSmelting> {
        private static final SimpleCookingSerializer<SmeltingRecipe> furnaceSerializer = RecipeSerializer.SMELTING_RECIPE;
        private final Supplier<Boolean> condition;

        public Serializer(Supplier<Boolean> condition){
            this.condition = condition;
        }

        public RecipeConditionSmelting fromJson(ResourceLocation recipeId, JsonObject json) {
            SmeltingRecipe furnaceRecipe = furnaceSerializer.fromJson(recipeId, json);
            return new RecipeConditionSmelting(furnaceRecipe, condition, this);
        }

        public RecipeConditionSmelting fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            SmeltingRecipe furnaceRecipe = furnaceSerializer.fromNetwork(recipeId, buffer);
            return new RecipeConditionSmelting(furnaceRecipe, condition, this);
        }

        public void toNetwork(FriendlyByteBuf buffer, RecipeConditionSmelting recipe) {
            furnaceSerializer.toNetwork(buffer, recipe);
        }


    }
}