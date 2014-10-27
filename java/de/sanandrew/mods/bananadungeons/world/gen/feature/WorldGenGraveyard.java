/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.bananadungeons.world.gen.feature;

import de.sanandrew.mods.bananadungeons.tileentity.TileEntityDungeonMobSpawner;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class WorldGenGraveyard
        extends WorldGenerator
{
    private static final ForgeDirection[] FACINGS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};

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
        int offX = x - 9 * facing.offsetX;
        int offZ = z - 9 * facing.offsetZ;
        for( int i = -1; i <= 1; i++ ) {
            for( int j = 0; j < 4; j++ ) {
                int iCalc = i * 3;
                int jCalc = 2 + j * 4;
                placeSingleGrave(world, rand, offX + iCalc * facing.offsetZ + jCalc * facing.offsetX, y, offZ + jCalc * facing.offsetZ + iCalc * facing.offsetX,
                                 facing);
            }
        }
    }

    private static void placeSingleGrave(World world, Random rand, int x, int y, int z, ForgeDirection facing) {
        world.setBlock(x, y, z, Blocks.dirt, 1, 2);
        world.setBlock(x + facing.offsetX, y, z + facing.offsetZ, Blocks.dirt, 1, 2);
        world.setBlock(x, y + 1, z, Blocks.cobblestone_wall, rand.nextInt(), 2);
        world.setBlock(x, y - 1, z, Blocks.mob_spawner, 0, 3);

        TileEntityDungeonMobSpawner spawner = new TileEntityDungeonMobSpawner();
        spawner.getSpawnerLogic().setEntityName("Zombie");
        spawner.getSpawnerLogic().spawnYShift = 2;
        world.setTileEntity(x, y - 1, z, spawner);
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
        for( int i = 196; i >= 48; i-- ) {
            Block block = world.getBlock(x, i, z);
            if( block != null && block == genBiome.topBlock ) {
                return i;
            }
        }

        return 0;
    }
}
