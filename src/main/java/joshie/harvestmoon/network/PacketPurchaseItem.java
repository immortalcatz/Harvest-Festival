package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.CalendarHelper;
import joshie.harvestmoon.helpers.PlayerHelper;
import joshie.harvestmoon.helpers.ShopHelper;
import joshie.harvestmoon.shops.IPurchaseable;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketPurchaseItem implements IMessage, IMessageHandler<PacketPurchaseItem, IMessage> {
    private int purchaseable_id;
    
    public PacketPurchaseItem() {}
    public PacketPurchaseItem(IPurchaseable purchaseable) {
        for(int i = 0; i < ShopInventory.registers.size(); i++) {
            IPurchaseable purchase = ShopInventory.registers.get(i);
            if(purchase.equals(purchaseable)) {
                purchaseable_id = i;
                break;
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(purchaseable_id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.purchaseable_id = buf.readInt();
    }
    
    @Override
    public IMessage onMessage(PacketPurchaseItem message, MessageContext ctx) {        
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        IPurchaseable purchaseable = ShopInventory.registers.get(message.purchaseable_id);
        if(purchaseable.canBuy(player.worldObj, PlayerHelper.getBirthday(player), CalendarHelper.getServerDate())) {
            ShopHelper.purchase(player, purchaseable.getProduct(), purchaseable.getCost());
        }
        
        return null;
    }
}