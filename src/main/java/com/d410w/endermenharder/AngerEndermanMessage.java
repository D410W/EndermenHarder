package com.d410w.endermenharder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AngerEndermanMessage {
    private final int endermanId;

    public AngerEndermanMessage(int endermanId) {
        this.endermanId = endermanId;
    }

    public static void encode(AngerEndermanMessage msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.endermanId);
    }

    public static AngerEndermanMessage decode(FriendlyByteBuf buffer) {
        return new AngerEndermanMessage(buffer.readInt());
    }

    public static void handle(AngerEndermanMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ServerLevel level = player.serverLevel();
                Entity entity = level.getEntity(msg.endermanId);

                if (entity instanceof EnderMan enderman) {
                    if (isValidTarget(player, enderman)) {
                        enderman.setTarget(player);
                        enderman.setLastHurtByPlayer(player);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static boolean isValidTarget(Player player, EnderMan enderman) {
        return !player.isSpectator()
                && !player.getInventory().armor.get(3).is(Items.CARVED_PUMPKIN)
                && player.hasLineOfSight(enderman)
                && enderman.distanceTo(player) <= 64.0;
    }
}