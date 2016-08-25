package joshie.harvest.cooking.items;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.cooking.Recipe;
import joshie.harvest.cooking.blocks.TileCooking;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.ItemHFBase;
import joshie.harvest.core.handlers.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class ItemCookbook extends ItemHFBase<ItemCookbook> implements ICreativeSorted {
    public ItemCookbook() {
        super(HFTab.COOKING);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!player.isSneaking()) {
            player.openGui(HarvestFestival.instance, GuiHandler.COOKBOOK, world, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 1000;
    }

    private static boolean isIngredient(ICookingIngredient ingredient, List<ICookingIngredient> set) {
        for (ICookingIngredient check: set) {
            if(ingredient.isEqual(check)) return true;
        }

        return false;
    }

    private static ItemStack getAndRemoveIngredient(ICookingIngredient ingredient, EntityPlayer player) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null && isIngredient(ingredient, HFApi.cooking.getCookingComponents(stack))) {
                ItemStack ret = stack.splitStack(1);
                if (stack.stackSize <= 0) player.inventory.mainInventory[i] = null; //Clear
                return ret;
            }
        }

        return null;
    }

    public static boolean cook(TileCooking cooking, EntityPlayer player, Recipe selected) {
        if (selected != null) {
            if (!CookingHelper.hasAllIngredients(selected, player)) return false;
            else {
                for (ICookingIngredient ingredient : selected.getRequiredIngredients()) {
                    ItemStack ret = getAndRemoveIngredient(ingredient, player);
                    if (ret == null) return false;
                    else cooking.addIngredient(ret);
                }

                return true;
            }
        } else return false;
    }
}
