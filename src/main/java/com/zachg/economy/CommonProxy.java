package com.zachg.economy;

import com.zachg.economy.api.EconomyAPI;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public class CommonProxy {

    private EconomyImpl economyImpl;

    public void preInit(FMLPreInitializationEvent event) {
        Economy.LOG.info("Economy mod initializing");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void serverStarting(FMLServerStartingEvent event) {
        if (economyImpl == null) {
            economyImpl = new EconomyImpl(event.getServer().getEntityWorld());
            EconomyAPI.setInstance(economyImpl);
            Economy.LOG.info("Economy API initialized and ready for use");
        } else {
            economyImpl.setWorld(event.getServer().getEntityWorld());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && economyImpl != null) {
            economyImpl.setWorld(event.world);
        }
    }
}
