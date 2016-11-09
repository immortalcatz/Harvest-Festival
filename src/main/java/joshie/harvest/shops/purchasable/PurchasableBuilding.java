package joshie.harvest.shops.purchasable;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PurchasableBuilding extends PurchasableBuilder {
    private final ResourceLocation resource;
    private final BuildingImpl building;
    private final String tooltip;

    public PurchasableBuilding(BuildingImpl building) {
        super(building.getCost(), building.getWoodCount(), building.getStoneCount(), building.getRegistryName());
        this.building = building;
        this.resource = BuildingRegistry.REGISTRY.getKey(building);
        this.tooltip = resource.getResourceDomain() + ".structures." + resource.getResourcePath() + ".tooltip";
    }

    public PurchasableBuilding(long cost, Building building, IRequirement... requirements) {
        super(cost, ((BuildingImpl)building).getRegistryName(), requirements);
        this.building = (BuildingImpl) building;
        this.resource = BuildingRegistry.REGISTRY.getKey(this.building);
        this.tooltip = resource.getResourceDomain() + ".structures." + resource.getResourcePath() + ".tooltip";
    }

    @Override
    public ItemStack getDisplayStack() {
        return building.getSpawner();
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        SpawnItemHelper.addToPlayerInventory(player, building.getBlueprint());
    }

    @Override
    public boolean isPurchaseable(World world, EntityPlayer player) {
        TownData town = TownHelper.getClosestTownToEntity(player);
        if (town.hasBuilding(resource)) return false;
        return building.getRules().canBuy(world, player, 1) && building.hasRequirements(player);
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return amount == 1 && super.canBuy(world, player, amount);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return (!TownHelper.getClosestTownToEntity(player).hasBuilding(resource) || building.canHaveMultiple()) && building.getRules().canBuy(world, player, 1) && building.hasRequirements(player);
    }

    @Override
    public String getDisplayName() {
        return building.getLocalisedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(TextFormatting.GOLD + getDisplayName());
        list.add("---------------------------");
        String tooltip = WordUtils.wrap(TextHelper.localize(this.tooltip.toLowerCase(Locale.ENGLISH)), 40);
        list.addAll(Arrays.asList(tooltip.split("\r\n")));
    }
}
