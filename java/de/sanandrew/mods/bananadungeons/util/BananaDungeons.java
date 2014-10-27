/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.bananadungeons.util;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import de.sanandrew.mods.bananadungeons.tileentity.TileEntityDungeonMobSpawner;
import de.sanandrew.mods.bananadungeons.world.gen.WorldGenerator;

@Mod(modid = BananaDungeons.MOD_ID, version = BananaDungeons.VERSION, name = "Banana Dungeons", dependencies = "required-after:sapmanpack@[2.1.0,)")
public class BananaDungeons
{
    public static final String MOD_ID = "bananadungeons";
    public static final String VERSION = "1.0";
    public static final String MOD_LOG = "BananaDungeons";

    @Instance(BananaDungeons.MOD_ID)
    public static BananaDungeons instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerTileEntity(TileEntityDungeonMobSpawner.class, MOD_ID + ":teDungeonSpawner");

        GameRegistry.registerWorldGenerator(new WorldGenerator(), 100);
    }
}
