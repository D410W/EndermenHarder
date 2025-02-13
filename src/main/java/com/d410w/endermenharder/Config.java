package com.d410w.endermenharder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Endermenharder.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static double configuredViewAngle = 0.2; // Default value

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue VIEW_ANGLE;

    static {
        BUILDER.push("Gameplay Settings");

        VIEW_ANGLE = BUILDER
                .comment("Anger angle in degrees: '0': 0°, '1': 180°, '2': 360°")
                .defineInRange("viewAngle", 0.2, 0.025, 2.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        // Update our cached value when config changes
        configuredViewAngle = VIEW_ANGLE.get();
    }
}
