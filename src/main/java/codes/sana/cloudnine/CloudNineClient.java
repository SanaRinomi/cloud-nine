package codes.sana.cloudnine;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.IWorld;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import codes.sana.cloudnine.client.CloudNineConfig;
import codes.sana.cloudnine.client.CloudNineConfigScreen;
import codes.sana.cloudnine.client.CloudNineRenderer;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CloudNineClient.MOD_ID)
public final class CloudNineClient
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String NAME = "Cloud Nine";
    public static final String MOD_ID = "cloudnine";

    public static ModConfig config;
    private static float cloudOffset;

    public CloudNineClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::config);

        MinecraftForge.EVENT_BUS.addListener(this::onload);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CloudNineConfig.getConfigSpec());

        ModLoadingContext.get().registerExtensionPoint(
            ExtensionPoint.CONFIGGUIFACTORY, 
            () -> (mc, screen) -> new CloudNineConfigScreen(screen));
    }

    private void config(final ModConfigEvent event)
    {
        config = event.getConfig();

        cloudOffset = (float)config.getConfigData().getIntOrElse("cloudnine.config.cloudOffset.value", 0);
    }

    private void onload(final Load event)
    {
        IWorld eworld = event.getWorld();

        if(eworld instanceof ClientWorld) {
            LOGGER.info("Starting cloud height change");
            ClientWorld cli = (ClientWorld)eworld;
            CloudNineRenderer cloud = new CloudNineRenderer();

            cli.effects().setCloudRenderHandler(cloud::render);
        }
    }

    public static float getCloudOffset() { return cloudOffset; }

    public static void setCloudOffset(float offset) {
        cloudOffset = offset;
    }

    public static void save() {
        config.getConfigData().set("cloudnine.config.cloudOffset.value", (int)cloudOffset);
        config.save();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
