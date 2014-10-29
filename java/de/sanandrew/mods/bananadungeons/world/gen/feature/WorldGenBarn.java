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
        carveOutAndGroundWall(world, x, y, z, currFacing);
        placeRoof(world, x, y, z, currFacing);
        placeStairs(world, x + 7 * currFacing.offsetX, y, z + 7 * currFacing.offsetZ, currFacing);
        placeStairs(world, x - 8 * currFacing.offsetX, y, z - 8 * currFacing.offsetZ, currFacing);
        placeFences(world, x, y, z, currFacing);
        placeHayballs(world, rand, x, y, z, currFacing);
        return false;
    }

    private static void carveOutAndGroundWall(World world, int x, int y, int z, ForgeDirection facing) {
        int logMetaWidth = facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH ? 4 : 8;
        int logMetaDepth = facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH ? 8 : 4;

        for( byte i = -9; i <= 9; i++ ) {
            for( byte j = -4; j <= 4; j++ ) {
                int offX = x + i * facing.offsetX + j * facing.offsetZ;
                int offZ = z + j * facing.offsetX + i * facing.offsetZ;

                for( byte k = 0; k <= 8; k++ ) {
                    world.setBlock(offX, y + k, offZ, Blocks.air, 0, 2);
                }

                world.setBlock(offX, y, offZ, Blocks.planks, 0, 2);

                if( i == -9 || i == 9 || j == -4 || j == 4 ) {
                    world.setBlock(offX, y + 1, offZ, Blocks.cobblestone, 0, 2);

                    if( (i == -9 || i == 9) && (j == -4 || j == 4) ) {
                        for( int h = 2; h <= 6; h++ ) {
                            world.setBlock(offX, y + h, offZ, Blocks.log, 0, 2);
                        }
                    } else {
                        world.setBlock(offX, y + 2, offZ, Blocks.planks, 0, 2);
                        world.setBlock(offX, y + 3, offZ, Blocks.planks, 0, 2);
                        world.setBlock(offX, y + 5, offZ, Blocks.planks, 0, 2);
                        world.setBlock(offX, y + 6, offZ, Blocks.planks, 0, 2);

                        if( i == -9 || i == 9 ) {
                            world.setBlock(offX, y + 4, offZ, Blocks.log, logMetaWidth, 2);
                        } else if( j == -4 || j == 4 ) {
                            world.setBlock(offX, y + 4, offZ, Blocks.log, logMetaDepth, 2);
                        }
                    }
                } else {
                    world.setBlock(offX, y + 4, offZ, Blocks.planks, 0, 2);
                }
            }
        }
    }

    private static void placeRoof(World world, int x, int y, int z, ForgeDirection facing) {
        int logMeta = facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH ? 8 : 4;

        for( byte i = 0; i <= 10; i++ ) {
            for( byte j = 0; j <= 5; j++ ) {
                byte offY = 6;
                if( j == 4 || j == 3 ) {
                    offY = 7;
                } else if( j == 2 || j == 1 || j == 0 ) {
                    offY = 8;
                }

                int offXPos = x + i * facing.offsetX + j * facing.offsetZ;
                int offZPos = z + j * facing.offsetX + i * facing.offsetZ;
                int offXNeg = x - i * facing.offsetX - j * facing.offsetZ;
                int offZNeg = z - j * facing.offsetX - i * facing.offsetZ;

                world.setBlock(offXPos, y + offY, offZPos, Blocks.log, logMeta, 2);
                world.setBlock(offXNeg, y + offY, offZNeg, Blocks.log, logMeta, 2);
                world.setBlock(offXPos, y + offY, offZNeg, Blocks.log, logMeta, 2);
                world.setBlock(offXNeg, y + offY, offZPos, Blocks.log, logMeta, 2);
            }
        }
    }

    private static void placeStairs(World world, int x, int y, int z, ForgeDirection facing) {
        int metaRight = 0;// east west south north
        int metaLeft = 0;
        int metaFront = 0;

        switch( facing ) {
            case NORTH: metaFront = 1; metaLeft = 2; metaRight = 3; break;
            case EAST: metaFront = 2; metaLeft = 1; metaRight = 0; break;
            case SOUTH: metaFront = 0; metaLeft = 3; metaRight = 2; break;
            case WEST: metaFront = 3; metaLeft = 0; metaRight = 1; break;
        }

        for( byte d = 0; d < 4; d++ ) {
            int offX = x + d * facing.offsetZ;
            int offZ = z + d * facing.offsetX;
            world.setBlock(offX, y + 4, offZ, Blocks.air, 0, 2);
            world.setBlock(offX + facing.offsetX, y + 4, offZ + facing.offsetZ, Blocks.air, 0, 2);
        }

        for( byte h = 0; h < 4; h++ ) {
            world.setBlock(x + h * facing.offsetZ, y + h + 1, z + h * facing.offsetX, Blocks.oak_stairs, metaFront, 2);
            world.setBlock(x + h * facing.offsetZ + facing.offsetX, y + h + 1, z + h * facing.offsetX + facing.offsetZ, Blocks.oak_stairs, metaFront, 2);
            for( int i = 3; i > 0; i-- ) {
                if( h < i ) {
                    int k = h + 4 - i;
                    world.setBlock(x + k * facing.offsetZ, y + h + 1, z + k * facing.offsetX, Blocks.oak_stairs, metaLeft, 2);
                    world.setBlock(x + k * facing.offsetZ + facing.offsetX, y + h + 1, z + k * facing.offsetX + facing.offsetZ, Blocks.oak_stairs, metaRight, 2);
                }
            }
        }
    }

    private static void placeFences(World world, int x, int y, int z, ForgeDirection facing) {
        for( int i = -6; i <= 6; i += 2 ) {
            int offX = x + i * facing.offsetX + 3 * facing.offsetZ;
            int offZ = z + 3 * facing.offsetX + i * facing.offsetZ;
            world.setBlock(offX, y + 1, offZ, Blocks.fence, 0, 2);
        }

        for( int i = -6; i <= 6; i += 4 ) {
            int offX = x + i * facing.offsetX;
            int offZ = z + i * facing.offsetZ;
            world.setBlock(offX + 2 * facing.offsetZ, y + 1, offZ + 2 * facing.offsetX, Blocks.fence, 0, 2);
            world.setBlock(offX + facing.offsetZ, y + 1, offZ + facing.offsetX, Blocks.fence, 0, 2);
        }
    }

    private static void placeHayballs(World world, Random rand, int x, int y, int z, ForgeDirection facing) {
        for( int i = 0; i < 30; i++ ) {
            int offX = x + (rand.nextInt(11) - 5) * facing.offsetX + (rand.nextInt(7) - 3) * facing.offsetZ;
            int offZ = z + (rand.nextInt(7) - 3) * facing.offsetX + (rand.nextInt(11) - 5) * facing.offsetZ;
            world.setBlock(offX, y + 5, offZ, Blocks.hay_block, rand.nextInt(3), 2);
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

        for( short i = 196; i >= 48; i-- ) {
            Block block = world.getBlock(x, i, z);
            if( block != null && block == genBiome.topBlock ) {
                return i;
            }
        }

        return 0;
    }
}
