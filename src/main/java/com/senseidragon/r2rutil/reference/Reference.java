/*
 * R2RUtils - Reference
 *
 *  Copyright (c) 2015. Jesse Patterson ("Senseidragon")
 *
 *  @author Senseidragon
 *  @license Lesser GNU Public License v3 (http://www.gnu.org/licensese/lgpl.html)
 */

package com.senseidragon.r2rutil.reference;

// Here is where I keep most of my constants; Chances are good I've missed a few.
// Lots of stuff from here ends up in the config file at some point.
public class Reference {
    public static final String MODID = "r2rutils";
    public static final String NAME = "R2R Utils";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "required-after:Forge@[10.13.4.1614,)";
    public static final String MINECRAFT = "[1.7.10,)";
    public static final String HEADER = "Refugee to Regent Utilities configuration\n" +
            "-----------------------------------------\n" +
            "Setting gameMode to 0 will have players suffer negative effects\n" +
            "when not wearing a helmet when under all but minimal overhead terrain.\n" +
            "Setting gameMode to 1 will enable an alternate ruleset which will allow\n" +
            "players to go up to 'ylevelOffset' levels under the surface per piece of armor\n" +
            "worn, starting with the helmet.\n" +
            "e.g., up to ylevelOffset below surface, no helmet required.\n" +
            "from ylevelOffset+1 to ylevelOffset+10, helmet required\n" +
            "helmet + chestpiece = ylevelOffset+11 to ylevelOffset+20\n" +
            "helmet + chestpiece + legs = ylevelOffset+21 to ylevelOffset+30\n" +
            "a full suit of armor allows the player to go to any depth without negative effect.\n" +
            "Note: Just wearing 3 pieces of armor doesn't mean you can go down ylevelOffset+30.\n" +
            "The armor must be in top down order -- Helm, Chest, Legs, Feet.  If Chest is missing,\n" +
            "player cannot go below ylevelOffset+20 regardless of other armor without suffering\n" +
            "negative effects.";
    public static final String GUIFACTORY = "com.senseidragon.r2rutil.utils.GuiFactory";
    public static final String CLIENT_PROXY = "com.senseidragon.r2rutil.proxy.ClientProxy";
    public static final String SERVER_PROXY = "com.senseidragon.r2rutil.proxy.ServerProxy";
    public static final String WEB_HOME = "http://senseidragon.com/R2RUtil/";
    public static int BUILD = 3;
    public static boolean isBlind = false;
    public static boolean isConfused = false;
    public static boolean isFatigued = true;
    public static boolean isSlowed = true;

    public static boolean updateCheck = true;

    public static int gameMode = 0;
    public static int ylevelOffset = 2;
}
