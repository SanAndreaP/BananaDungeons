/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.bananadungeons.tileentity;

import de.sanandrew.core.manpack.util.SAPReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

public abstract class AMobSpawnerDungeonLogic
    extends MobSpawnerBaseLogic
{
    public int spawnYShift = 0;

    @Override
    public void updateSpawner() {
        if( this.isActivated() ) {
            if( this.getSpawnerWorld().isRemote ) {
                double partX = this.getSpawnerX() + this.getSpawnerWorld().rand.nextFloat();
                double partY = this.getSpawnerY() + this.spawnYShift + this.getSpawnerWorld().rand.nextFloat();
                double partZ = this.getSpawnerZ() + this.getSpawnerWorld().rand.nextFloat();
                this.getSpawnerWorld().spawnParticle("smoke", partX, partY, partZ, 0.0D, 0.0D, 0.0D);
                this.getSpawnerWorld().spawnParticle("flame", partX, partY, partZ, 0.0D, 0.0D, 0.0D);

                if( this.spawnDelay > 0 ) {
                    --this.spawnDelay;
                }

                this.field_98284_d = this.field_98287_c;
                this.field_98287_c = (this.field_98287_c + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0D;
            } else {
                if( this.spawnDelay == -1 ) {
                    this.resetTimer();
                }

                if( this.spawnDelay > 0 ) {
                    this.spawnDelay--;
                    return;
                }

                boolean spawned = false;

                int spawnCount = this.getSpawnCount();
                for( int i = 0; i < spawnCount; i++ ) {
                    Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
                    int spawnRange = this.getSpawnRange();

                    if( entity == null ) {
                        return;
                    }

                    int j = this.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), this.getSpawnerAABB().expand(spawnRange * 2, 4.0D, spawnRange * 2)).size();

                    if( j >= this.getMaxNearbyEntities() ) {
                        this.resetTimer();
                        return;
                    }

                    double entityX = this.getSpawnerX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * spawnRange;
                    double entityY = (this.getSpawnerY() + this.spawnYShift + this.getSpawnerWorld().rand.nextInt(3) - 1);
                    double entityZ = this.getSpawnerZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * spawnRange;

                    entity.setLocationAndAngles(entityX, entityY, entityZ, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

                    if( entity instanceof EntityLiving || this.canSpawnHere(entity) ) {
                        this.func_98265_a(entity);
                        this.getSpawnerWorld().playAuxSFX(2004, this.getSpawnerX(), this.getSpawnerY(), this.getSpawnerZ(), 0);

                        if( entity instanceof EntityLiving ) {
                            ((EntityLiving) entity).spawnExplosionParticle();
                        }

                        spawned = true;
                    }
                }

                if( spawned ) {
                    this.resetTimer();
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.spawnYShift = nbt.getInteger("SpawnerYShift");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("SpawnerYShift", this.spawnYShift);
    }

    private void resetTimer() {
        SAPReflectionHelper.invokeCachedMethod(MobSpawnerBaseLogic.class, this, "resetTimer", "resetTimer", null, null);
    }

    private int getSpawnCount() {
        return SAPReflectionHelper.getCachedFieldValue(MobSpawnerBaseLogic.class, this, "spawnCount", "spawnCount");
    }

    private int getSpawnRange() {
        return SAPReflectionHelper.getCachedFieldValue(MobSpawnerBaseLogic.class, this, "spawnRange", "spawnRange");
    }

    private int getMaxNearbyEntities() {
        return SAPReflectionHelper.getCachedFieldValue(MobSpawnerBaseLogic.class, this, "maxNearbyEntities", "maxNearbyEntities");
    }

    public boolean canSpawnHere(Entity entity) {
        return this.getSpawnerWorld().checkNoEntityCollision(entity.boundingBox)
               && this.getSpawnerWorld().getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty()
               && !this.getSpawnerWorld().isAnyLiquid(entity.boundingBox)
               && this.getSpawnerWorld().difficultySetting != EnumDifficulty.PEACEFUL;
    }

    private AxisAlignedBB getSpawnerAABB() {
        return AxisAlignedBB.getBoundingBox(this.getSpawnerX(), this.getSpawnerY() + this.spawnYShift, this.getSpawnerZ(), this.getSpawnerX() + 1,
                                     this.getSpawnerY() + this.spawnYShift + 1, this.getSpawnerZ() + 1);
    }
}
