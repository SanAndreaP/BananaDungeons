/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.bananadungeons.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenBarn
        extends WorldGenerator
{
    private static final ForgeDirection[] FACINGS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
    public static final List<String> BIOMES = new ArrayList<>(1);

    static {
        BIOMES.add("Forest");
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        ForgeDirection currFacing = FACINGS[rand.nextInt(FACINGS.length)];
        carveOutAndGroundWall(world, rand, x, y, z, currFacing);
        return false;
    }

    private static void carveOutAndGroundWall(World world, Random rand, int x, int y, int z, ForgeDirection facing) {
        for( int i = -9; i <= 9; i++ ) {
            for( int j = -3; j <= 3; j++ ) {
                int offX = x + i * facing.offsetX + j * facing.offsetZ;
                int offZ = z + j * facing.offsetX + i * facing.offsetZ;
                // catrve out area
                for( int k = 0; k <= 8; k++ ) {
                    world.setBlock(offX, y + k, offZ, Blocks.air, 0, 2);
                }

                // lay out bottom with oak planks on layer 0 and 4
                world.setBlock(offX, y, offZ, Blocks.planks, 0, 2);
                world.setBlock(offX, y + 4, offZ, Blocks.planks, 0, 2);

                if( !(i > -9 && i < 9 && j > -3 && j < 3) ) {
                    world.setBlock(offX, y + 1, offZ, Blocks.cobblestone, 0, 2);
                    world.setBlock(offX, y + 2, offZ, Blocks.planks, 0, 2);
                    world.setBlock(offX, y + 3, offZ, Blocks.planks, 0, 2);
                    world.setBlock(offX, y + 5, offZ, Blocks.planks, 0, 2);
                    world.setBlock(offX, y + 6, offZ, Blocks.planks, 0, 2);
                }
//                    // place podzol in inner area
//                    world.setBlock(offX, y, offZ, Blocks.dirt, 2, 2);
//                } else {
//                    // place wall around podzol
//                    world.setBlock(offX, y, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
//                    world.setBlock(offX, y + 1, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
//                    world.setBlock(offX, y + 4, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
//                    if( (i == -9 || i == 9) && (j == -6 || j == 6) ) {
//                        world.setBlock(offX, y + 2, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
//                        world.setBlock(offX, y + 3, offZ, Blocks.stonebrick, rand.nextInt(2) + 1, 2);
//                    } else {
//                        world.setBlock(offX, y + 2, offZ, Blocks.iron_bars, 0, 2);
//                        world.setBlock(offX, y + 3, offZ, Blocks.iron_bars, 0, 2);
//                    }
//                }
            }
        }
    }

    public static void generate(World world, Random rand, int chunkX, int chunkZ) {
        int x = (chunkX << 4) + rand.nextInt(16);
        int z = (chunkZ << 4) + rand.nextInt(16);
        int y = getSuitableY(world, x, z);

        if( y > 0 ) {
            (new WorldGenBarn()).generate(world, rand, x, y, z);
        }
    }

    private static int getSuitableY(World world, int x, int z) {
        BiomeGenBase genBiome = world.getBiomeGenForCoords(x, z);
        if( !BIOMES.contains(genBiome.biomeName) ) {
//            return 0;
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
