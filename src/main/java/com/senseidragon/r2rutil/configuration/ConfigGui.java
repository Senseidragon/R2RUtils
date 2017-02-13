/*
 * R2RUtils - ConfigGui
 *
 *  Copyright (c) 2015. Jesse Patterson ("Senseidragon")
 *
 *  @author Senseidragon
 *  @license Lesser GNU Public License v3 (http://www.gnu.org/licensese/lgpl.html)
 */

package com.senseidragon.r2rutil.configuration;


import com.senseidragon.r2rutil.handlers.ConfigurationHandler;
import com.senseidragon.r2rutil.reference.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

// Create a Gui for the main screen "Mod Options" area
// I learned this from MinAlien's Forge Tutorial Spotlight on Config GUIs
// You can find an archive of the tutorial here:
// https://github.com/Minalien/BlogArchive/blob/master/ForgeTutorials/Spotlight__Config_GUIs.md
public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parent) {
        super(parent, new ConfigElement(ConfigurationHandler.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configFile.toString()));
    }
}
