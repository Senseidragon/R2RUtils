/*
 * R2RUtils - R2RHandler
 *
 *  Copyright (c) 2015. Jesse Patterson ("Senseidragon")
 *
 *  @author Senseidragon
 *  @license Lesser GNU Public License v3 (http://www.gnu.org/licensese/lgpl.html)
 */

package com.senseidragon.r2rutil.handlers;

import com.senseidragon.r2rutil.reference.Reference;
import com.senseidragon.r2rutil.utils.LogHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class R2RHandler {
    public static String updates = "";
    public static boolean outdated = false;

    // Check for a build version higher than the one installed
    public static void checkForUpdates() {
        int currentVersion = Reference.BUILD;
        // pull current version number from Internet
        int nextVersion = getNewest();

        // Was there a newer version?
        // also, if we get -1 from nextVersion, we set to false
        if (currentVersion < nextVersion) {
            // Grab the changelog text for latest version
            updates = getUpdate(nextVersion);
            // Set outdated flag to inform new players logging in
            outdated = true;
        } else {
            outdated = false;
        }
    }

    // Kind of a big function for "grab a single file off the Internet", but meh.
    public static int getNewest() {
        LogHelper.info("Checking version information");
        try {
            // latest.txt contains a single (integer) build number
            // I just manually increment it every release
            URL url = new URL(Reference.WEB_HOME + "latest.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return Integer.parseInt(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static String getUpdate(int version) {
        try {
            // Grab the <build_number>.txt file from the Internet
            // Which contains the release notes for that version
            URL url = new URL(Reference.WEB_HOME + version + ".txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Error";
    }

    // When player logs in, if a newer update is available, print the update text in their chat window
    @SubscribeEvent
    public void checkUpdate(PlayerEvent.PlayerLoggedInEvent event) {
        if (R2RHandler.outdated) {
            event.player.addChatComponentMessage(new ChatComponentText("An updated version of R2RUtils is available!"));
            event.player.addChatComponentMessage(new ChatComponentText(R2RHandler.updates));
        }
    }

    @SubscribeEvent
    public void onR2RHandler(LivingEvent.LivingUpdateEvent event) {
        // Only apply effects to players, not mobs
        if (event.entityLiving instanceof EntityPlayerMP) {
            World world = event.entity.worldObj;
            long worldTime = world.getTotalWorldTime();
            // Check about once every 4 seconds so as to not be a CPU hog
            if (worldTime % 80 == 0) {
                EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
                int blockCount = 0;
                String name = player.getDisplayName();
                int posX = (int) Math.floor(player.posX);
                int posY = (int) Math.floor(player.posY);
                int posZ = (int) Math.floor(player.posZ);

                // Original, simple ruleset.
                if (Reference.gameMode == 0) {
                    // If you aren't wearing a helmet
                    if (player.inventory.armorInventory[3] == null) {
                        // Check all blocks from above your head to the sky
                        for (int i = posY + 2; i < 256; i++) {
                            Block block = world.getBlock(posX, i, posZ);
                            // If they aren't air or leaves, they count as stuff
                            // that can fall on your head
                            if (block != Blocks.air && block != Blocks.leaves) {
                                // If it is stone, dirt, grass, gravel, sand, or sandstone
                                // Make it "triple" dangerous - It tends to be more strict
                                // on natural caves than say a small one-story hovel.
                                if (block == Blocks.stone) blockCount += 3;
                                else if (block == Blocks.dirt) blockCount += 3;
                                else if (block == Blocks.grass) blockCount += 3;
                                else if (block == Blocks.gravel) blockCount += 3;
                                else if (block == Blocks.sand) blockCount += 3;
                                else if (block == Blocks.sandstone) blockCount += 3;
                                    // Other blocks count, just not as much
                                else blockCount++;
                            }
                        }

                        // Too much stuff over your head, you freak out
                        if (blockCount > 6) {
                            player.addChatComponentMessage(new ChatComponentText("The blocks over your head don't look too safe, wear a helmet! "));
                            afflictPlayer(player);
                        }
                    }
                    // new ruleset.
                } else if (Reference.gameMode == 1) {
                    Integer surface = getSurfaceYLevel(world, posX, posY-1, posZ);
                    // "depth" is how many blocks of solid, opaque material are between you and sky
                    Integer depth = blocksBetween(world, posX, posY, posZ, surface);
                    Integer offset = Reference.ylevelOffset;
                    // Nest checks because later ones are irrelevant if earlier piece is missing
                    // Is the amount of "overhead" stuff greater than configured tolerance?
                    // Tolerance - start wearing helm
                    if (depth > offset) {
                        // Check for helm
                        if (player.inventory.armorInventory[3] == null) {
                            player.addChatComponentMessage(new ChatComponentText("Without your helmet, you start to panic!"));
                            afflictPlayer(player);
                        }

                        // Tolerance+10 - start wearing chest
                        if (depth > offset + 10) {
                            // Check for chest
                            if (player.inventory.armorInventory[2] == null) {
                                player.addChatComponentMessage(new ChatComponentText("You feel unprotected without your chest armor.  Your breathing becomes labored..."));
                                afflictPlayer(player);
                            }

                            // Tolerance+20 - start wearing legs
                            if (depth > offset + 20) {
                                // Check for legs
                                if (player.inventory.armorInventory[1] == null) {
                                    player.addChatComponentMessage(new ChatComponentText("Your unborn children cry for protection as you find yourself pantless."));
                                    afflictPlayer(player);
                                }

                                // Tolerance+30 - start wearing boots
                                if (depth > offset + 30) {
                                    // Check for boots
                                    if (player.inventory.armorInventory[0] == null) {
                                        player.addChatComponentMessage(new ChatComponentText("This far underground, a foot injury could prove fatal.  Put on some boots!"));
                                        afflictPlayer(player);
                                    }
                                }
                            }
                        }
                    }
                    // If wearing full armor, no effects are applied
                }
            }
        }
    }

    // Supported potion effects for when player is "freaked out"
    // More than one effect can be applied at a time
    private void afflictPlayer(EntityPlayerMP player) {
        PotionEffect effect;
        // Blindness I potion effect
        if (Reference.isBlind) {
            effect = new PotionEffect(Potion.blindness.id, 100, 0);
            player.addPotionEffect(effect);
        }
        // Nausea I potion effect
        if (Reference.isConfused) {
            effect = new PotionEffect(Potion.confusion.id, 100, 0);
            player.addPotionEffect(effect);
        }
        // Slowness I potion effect
        if (Reference.isSlowed) {
            effect = new PotionEffect(Potion.moveSlowdown.id, 100, 0);
            player.addPotionEffect(effect);
        }
        // Mining Fatigue V potion effect
        if (Reference.isFatigued) {
            effect = new PotionEffect(Potion.digSlowdown.id, 100, 4);
            player.addPotionEffect(effect);
        }
    }

    // returns number of opaque, non-tree blocks between player's feet and the "surface"
    private int blocksBetween(World world, int posX, int posY, int posZ, int surface) {
        Integer between = 0;
        //posZ -= 1;

        //String loc = String.format("%d, %d, %d  Surf: %d", posX, posY, posZ, surface);

        //LogHelper.info("Loc: " + loc);
        for (int i = surface; i > posY; i--) {
            Block block = world.getBlock(posX, i, posZ);
            if (block.isOpaqueCube() &&
                    !block.isAir(world, posX, i, posZ) &&
                    !block.isLeaves(world, posX, i, posZ) &&
                    !block.isWood(world, posX, i, posZ)) between++;
        }
        //LogHelper.info("Between: " + between.toString());
        return between;
    }

    // Returns yLevel of highest point with two simultaneous opaque blocks
    private int getSurfaceYLevel(World world, int posX, int posY, int posZ) {
        int surfaceY = posY;

        // From sky to player feet
        for (int i = 255; i > posY; i--) {
            // z-1 because for some reason players posZ seems to be off by
            // one in the world.  Look at Z pos in F3 screen. number in () is off by one.
            Block block = world.getBlock(posX, i, posZ);
            //LogHelper.info(block.getUnlocalizedName());
            // Find first opaque block, then check for the one immediately under it.
            // When you find two in a row, that's the "surface" for that posY.
            if (block.isOpaqueCube()) {
                Block block2 = world.getBlock(posX, i - 1, posZ);

                if (block2.isOpaqueCube()) {
                    surfaceY = i;
                    break;
                }
            }
        }

        return surfaceY;
    }
}
