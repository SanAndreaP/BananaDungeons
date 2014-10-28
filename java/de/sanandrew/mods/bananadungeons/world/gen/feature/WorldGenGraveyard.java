/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.bananadungeons.world.gen.feature;

import de.sanandrew.mods.bananadungeons.tileentity.SpawnerEntry;
import de.sanandrew.mods.bananadungeons.tileentity.TileEntityDungeonMobSpawner;
import de.sanandrew.mods.bananadungeons.util.BananaDungeons;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenGraveyard
        extends WorldGenerator
{
    private static final ForgeDirection[] FACINGS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
    private static final SpawnerEntry ZOMBIE = SpawnerEntry.newEntry("Zombie").addHelmet(new ItemStack(Items.leather_helmet), 0.0F)
                                                                              .addHeldItem(new ItemStack(Items.iron_axe), 0.0F);
    private static final SpawnerEntry SKELLIE_BOW = SpawnerEntry.newEntry("Skeleton").addHelmet(new ItemStack(Items.leather_helmet), 0.0F)
                                                                                     .addHeldItem(new ItemStack(Items.bow), 0.0F);
    private static final SpawnerEntry SKELLIE_SWORD = SpawnerEntry.newEntry("Skeleton").addHelmet(new ItemStack(Items.golden_helmet), 0.0F)
                                                                                       .addHeldItem(new ItemStack(Items.iron_sword), 0.0F);

    public static final String CHEST_CONTENT = BananaDungeons.MOD_ID + ":graveyard";
    public static final List<String> BIOMES = new ArrayList<>(1);

    static {
        ChestGenHooks.getInfo(CHEST_CONTENT).setMin(10);
        ChestGenHooks.getInfo(CHEST_CONTENT).setMax(18);

        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.rotten_flesh, 0, 4, 8, 15));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.poisonous_potato, 0, 4, 8, 15));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.bone, 0, 4, 8, 15));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.dye, 15, 2, 6, 15));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 10, 10));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.gold_nugget, 0, 1, 9, 10));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 8, 10));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.emerald, 0, 1, 5, 5));
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.golden_carrot, 0, 1, 1, 5));

        ItemStack sword = new ItemStack(Items.iron_sword, 1);
        sword.addEnchantment(Enchantment.looting, 1);
        sword.addEnchantment(Enchantment.unbreaking, 2);
        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(sword, 1, 1, 5));

        ChestGenHooks.addItem(CHEST_CONTENT, new WeightedRandomChestContent(Items.diamond, 0, 3, 6, 5));

        BIOMES.add("Plains");
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        ForgeDirection currFacing = FACINGS[rand.nextInt(FACINGS.length)];
        carveOutAndGroundWall(world, rand, x, y, z, currFacing);
        placeDoorway(world, rand, x, y, z, currFacing);
        placeGraves(world, rand, x, y, z, currFacing);
        return true;
    }

    private static void carveOutAndGroundWall(World world, Random rand, int x, int y, int z, ForgeDirection facing) {
        for( int i = -9; i <= 9; i++ ) {
            for( int j = -6; j <= 6; j++ ) {
                int offX = x + i * facing.offsetX + j * facing.offsetZ;
                int offZ = z + j * facing.offsetX + i * facing.offsetZ;
                // catrve out area
                for( int k = -1; k <= 4; k++ ) {
                    world.setBlock(offX, y + k, offZ, Blocks.air, 0, 2);
                }

                // lay out bottom with cobblestone/mossy cobble
                world.setBlock(offX, y - 1, offZ, rand.nextBoolean() ? Blocks.cobblestone : Blocks.mossy_cobblestone, 0, 2);

                if( i > -9 && i < 9 && j > -6 && j < 6 ) {
                    // place podzol in inner area
                    world.setBlock(offX, y, offZ, Blocks.dirt, 2, 2);
                } else {
                    // place wall around podzol
                    world.setBlock(offX, y, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
                    world.setBlock(offX, y + 1, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
                    world.setBlock(offX, y + 4, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
                    if( (i == -9 || i == 9) && (j == -6 || j == 6) ) {
                        world.setBlock(offX, y + 2, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
                        world.setBlock(offX, y + 3, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
                    } else {
                        world.setBlock(offX, y + 2, offZ, Blocks.iron_bars, 0, 2);
                        world.setBlock(offX, y + 3, offZ, Blocks.iron_bars, 0, 2);
                    }
                }
            }
        }
    }

    private static void placeDoorway(World world, Random rand, int x, int y, int z, ForgeDirection facing) {
        int offX = x + 9 * facing.offsetX;
        int offZ = z + 9 * facing.offsetZ;

        for( int w = -2; w <= 2; w++ ) {
            for( int h = 1; h <= 3; h++ ) {
                if( w == -2 || w == 2 ) {
                    world.setBlock(offX + w * facing.offsetZ, y + h, offZ + w * facing.offsetX, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
                } else {
                    world.setBlock(offX + w * facing.offsetZ, y + h, offZ + w * facing.offsetX, Blocks.air, 0, 2);
                }
            }
        }
    }

    private static void placeGraves(World world, Random rand, int x, int y, int z, ForgeDirection facing) {
        int chestCnt = rand.nextInt(3) + 1;
        boolean[][] chests = new boolean[3][4];
        for( int i = 0; i < chestCnt; i++) {
            chests[rand.nextInt(3)][rand.nextInt(4)] = true;
        }

        int offX = x - 9 * facing.offsetX;
        int offZ = z - 9 * facing.offsetZ;
        for( int i = -1; i <= 1; i++ ) {
            for( int j = 0; j < 4; j++ ) {
                int iCalc = i * 3;
                int jCalc = 2 + j * 4;
                placeSingleGrave(world, rand, offX + iCalc * facing.offsetZ + jCalc * facing.offsetX, y, offZ + jCalc * facing.offsetZ + iCalc * facing.offsetX,
                                 facing, chests[i+1][j]);
            }
        }
    }

    private static void placeSingleGrave(World world, Random rand, int x, int y, int z, ForgeDirection facing, boolean hasLoot) {
        int offX = x + facing.offsetX;
        int offZ = z + facing.offsetZ;

        world.setBlock(x, y, z, Blocks.dirt, 1, 2);
        world.setBlock(offX, y, offZ, Blocks.dirt, 1, 2);
        world.setBlock(x, y + 1, z, Blocks.cobblestone_wall, rand.nextInt(), 2);

        if( hasLoot && world.getBlock(offX, y - 1, offZ) != Blocks.chest) {
            world.setBlock(offX, y - 1, offZ, Blocks.chest, 0, 3);
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(offX, y - 1, offZ);
            WeightedRandomChestContent.generateChestContents(rand, ChestGenHooks.getItems(CHEST_CONTENT, rand), chest, ChestGenHooks.getCount(CHEST_CONTENT, rand));
            chest.markDirty();
        } else {
            world.setBlock(offX, y - 1, offZ, Blocks.mob_spawner, 0, 3);
            TileEntityDungeonMobSpawner spawner = new TileEntityDungeonMobSpawner();
            spawner.getSpawnerLogic().spawnYShift = 3;

            int chance = rand.nextInt(10);
            if( chance < 2 ) {
                SKELLIE_SWORD.setupSpawner(spawner);
            } else if( chance < 5 ) {
                SKELLIE_BOW.setupSpawner(spawner);
            } else {
                ZOMBIE.setupSpawner(spawner);
            }

            world.setTileEntity(offX, y - 1, offZ, spawner);
        }
    }

    public static void generate(World world, Random rand, int chunkX, int chunkZ) {
        int x = (chunkX << 4) + rand.nextInt(16);
        int z = (chunkZ << 4) + rand.nextInt(16);
        int y = getSuitableY(world, x, z);

        if( y > 0 ) {
            (new WorldGenGraveyard()).generate(world, rand, x, y, z);
        }
    }

    private static int getSuitableY(World world, int x, int z) {
        BiomeGenBase genBiome = world.getBiomeGenForCoords(x, z);
        if( !BIOMES.contains(genBiome.biomeName) ) {
            return 0;
        }

        for( int i = 196; i >= 48; i-- ) {
            Block block = world.getBlock(x, i, z);
            if( block != null && block == genBiome.topBlock ) {
                return i;
            }
        }

        return 0;
    }
}
