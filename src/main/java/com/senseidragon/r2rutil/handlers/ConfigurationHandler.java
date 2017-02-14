/*
 * R2RUtils - ConfigurationHandler
 *
 *  Copyright (c) 2015. Jesse Patterson ("Senseidragon")
 *
 *  @author Senseidragon
 *  @license Lesser GNU Public License v3 (http://www.gnu.org/licensese/lgpl.html)
 */

package com.senseidragon.r2rutil.handlers;

import com.senseidragon.r2rutil.reference.Reference;
import com.senseidragon.r2rutil.utils.LogHelper;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler {
    public static Configuration configFile;

    // Load config file and sync immediately just in case some (or all)
    // values were missing from the config file
    public static void init(File _configFile) {
        configFile = new Configuration(_configFile);
        try {
            configFile.load();
            syncConfig();
        } catch (Exception e) {
            LogHelper.info(e.getStackTrace());
        } finally {
            if (configFile.hasChanged()) configFile.save();
        }
    }

    // Load data from config or save it with default values as appropriate.
    public static void syncConfig() {
        configFile.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, Reference.HEADER);
        Reference.gameMode = configFile.getInt("gameMode", Configuration.CATEGORY_GENERAL, Reference.gameMode, 0, 1, "Mode 0 = Original Rules, Mode 1 = New Rules");
        Reference.isBlind = configFile.getBoolean("isBlind", Configuration.CATEGORY_GENERAL, Reference.isBlind, "Blindness effect enabled");
        Reference.isConfused = configFile.getBoolean("isConfused", Configuration.CATEGORY_GENERAL, Reference.isConfused, "Nausea effect enabled");
        Reference.isFatigued = configFile.getBoolean("isFatigued", Configuration.CATEGORY_GENERAL, Reference.isFatigued, "Mining Fatigue effect enabled");
        Reference.isSlowed = configFile.getBoolean("isSlowed", Configuration.CATEGORY_GENERAL, Reference.isSlowed, "Slowness effect enabled");
        Reference.ylevelOffset = configFile.getInt("ylevelOffset", Configuration.CATEGORY_GENERAL, Reference.ylevelOffset, 2, 64, "Under how much overhead should negative effects start");
        Reference.updateCheck = configFile.getBoolean("updateCheck", Configuration.CATEGORY_GENERAL, Reference.updateCheck, "Set to false to disable checking for mod updates");
        // Only save if you actually change something
        if (configFile.hasChanged()) configFile.save();
    }
}
