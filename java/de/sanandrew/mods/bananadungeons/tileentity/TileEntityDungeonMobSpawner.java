/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.bananadungeons.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class TileEntityDungeonMobSpawner
    extends TileEntityMobSpawner
{
    private final AMobSpawnerDungeonLogic spawnLogic = new AMobSpawnerDungeonLogic()
    {
        @Override
        public void func_98267_a(int eventId) {
            TileEntityDungeonMobSpawner.this.worldObj.addBlockEvent(TileEntityDungeonMobSpawner.this.xCoord, TileEntityDungeonMobSpawner.this.yCoord,
                                                                    TileEntityDungeonMobSpawner.this.zCoord, Blocks.mob_spawner, eventId, 0);
        }

        @Override
        public World getSpawnerWorld() {
            return TileEntityDungeonMobSpawner.this.worldObj;
        }

        @Override
        public int getSpawnerX() {
            return TileEntityDungeonMobSpawner.this.xCoord;
        }

        @Override
        public int getSpawnerY() {
            return TileEntityDungeonMobSpawner.this.yCoord;
        }

        @Override
        public int getSpawnerZ() {
            return TileEntityDungeonMobSpawner.this.zCoord;
        }

        @Override
        public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart rndMinecart) {
            super.setRandomEntity(rndMinecart);

            if( this.getSpawnerWorld() != null ) {
                this.getSpawnerWorld().markBlockForUpdate(TileEntityDungeonMobSpawner.this.xCoord, TileEntityDungeonMobSpawner.this.yCoord,
                                                          TileEntityDungeonMobSpawner.this.zCoord);
            }
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.spawnLogic.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.spawnLogic.writeToNBT(nbt);
    }

    @Override
    public void updateEntity() {
        this.spawnLogic.updateSpawner();
        super.updateEntity();
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        return this.spawnLogic.setDelayToMin(id) || super.receiveClientEvent(id, value);
    }

    @Override
    public MobSpawnerBaseLogic func_145881_a() {
        return this.spawnLogic;
    }

    public AMobSpawnerDungeonLogic getSpawnerLogic() {
        return this.spawnLogic;
    }
}
