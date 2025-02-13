package com.d410w.endermenharder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "endermenharder", value = Dist.CLIENT)
public class ClientEventHandler {

    static double viewAngle = Config.configuredViewAngle;

    private static final double VIEW_ANGLE = 1.0F - viewAngle;
    private static final double MAX_DISTANCE = 64.0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player != null && mc.level != null) {
                checkForEndermenInView(player, mc.level);
            }
        }
    }

    private static void checkForEndermenInView(LocalPlayer player, Level level) {
        // Prevent checking in creative/spectator modes
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        Vec3 playerPos = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        Vec3 lookVec = player.getLookAngle();

        for (EnderMan enderman : level.getEntitiesOfClass(EnderMan.class,
                player.getBoundingBox().inflate(MAX_DISTANCE))) {

            Vec3 endermanPos = new Vec3(enderman.getX(), enderman.getEyeY(), enderman.getZ());
            Vec3 toEnderman = endermanPos.subtract(playerPos).normalize();

            double dot = lookVec.dot(toEnderman);
            if ( dot > VIEW_ANGLE / toEnderman.length() ) {
                NetworkHandler.INSTANCE.sendToServer(new AngerEndermanMessage(enderman.getId()));
            }
        }
    }
}
