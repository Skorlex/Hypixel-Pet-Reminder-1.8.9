package com.github.skorlex.hypixelpetreminder;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "hypixelpetreminder", useMetadata = true)
public class ExampleMod {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Initialize our configuration file
        PetConfig.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the event listener for chat and tick events
        MinecraftForge.EVENT_BUS.register(new PetEventHandler());

        // Register the client-side command
        ClientCommandHandler.instance.registerCommand(new PetCommand());
    }
}