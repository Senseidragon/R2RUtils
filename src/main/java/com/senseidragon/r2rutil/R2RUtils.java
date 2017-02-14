/*
 * R2RUtils - R2RUtils
 *
 *  Copyright (c) 2015. Jesse Patterson ("Senseidragon")
 *
 *  @author Senseidragon
 *  @license Lesser GNU Public License v3 (http://www.gnu.org/licensese/lgpl.html)
 */

package com.senseidragon.r2rutil;

import com.senseidragon.r2rutil.handlers.ConfigurationHandler;
import com.senseidragon.r2rutil.handlers.R2RHandler;
import com.senseidragon.r2rutil.proxy.IProxy;
import com.senseidragon.r2rutil.reference.Reference;
import com.senseidragon.r2rutil.utils.LogHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.senseidragon.r2rutil.handlers.ConfigurationHandler.syncConfig;


@Mod(modid = Reference.MODID,
        name = Reference.NAME,
        version = Reference.VERSION,
        dependencies = Reference.DEPENDENCIES,
        acceptedMinecraftVersions = Reference.MINECRAFT,
        guiFactory = Reference.GUIFACTORY)
public class R2RUtils {
    @Mod.Instance(Reference.MODID)
    public static R2RUtils instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static IProxy proxy;

    //    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load network handling and mod initialization
        LogHelper.info("Reading configuration settings");
        proxy.registerRenderers();
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        syncConfig();
        R2RHandler handler = new R2RHandler();
        MinecraftForge.EVENT_BUS.register(handler); // for the entityEvent event

        LogHelper.info("Configuration complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // run after all other mods have preinitialized
        LogHelper.info("Initializing");
        MinecraftForge.EVENT_BUS.register(instance); // for the entityEvent event
        if (Reference.updateCheck) R2RHandler.checkForUpdates();
        LogHelper.info("Completed initialization");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // run after all other mods have initialized
        //LogHelper.info("Entering postInit()");
        //LogHelper.info("Exiting postInit()");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        LogHelper.info("Configuration changes applied");
        if (event.modID.equals(Reference.MODID)) syncConfig();
    }
}
