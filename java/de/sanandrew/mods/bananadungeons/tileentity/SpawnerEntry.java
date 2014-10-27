/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.bananadungeons.tileentity;

import de.sanandrew.core.manpack.util.javatuples.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class SpawnerEntry
{
    private Pair<ItemStack, Float> helmet;
    private Pair<ItemStack, Float> chestplate;
    private Pair<ItemStack, Float> leggings;
    private Pair<ItemStack, Float> boots;
    private Pair<ItemStack, Float> weapon;

    private List<PotionEffect> potions = new ArrayList<>(0);

    private SpawnerEntry() { }

    public void setupSpawner(TileEntityDungeonMobSpawner spawner) {
        NBTTagCompound entityNBT = new NBTTagCompound();
        NBTTagList equipment = new NBTTagList();
        NBTTagList dropChance = new NBTTagList();

        writeItemToNbtList(this.weapon, equipment, dropChance);
        writeItemToNbtList(this.boots, equipment, dropChance);
        writeItemToNbtList(this.leggings, equipment, dropChance);
        writeItemToNbtList(this.chestplate, equipment, dropChance);
        writeItemToNbtList(this.helmet, equipment, dropChance);

        entityNBT.setTag("Equipment", equipment);
        entityNBT.setTag("DropChances", dropChance);

        for( PotionEffect effect : this.potions ) {

        }

        spawner.getSpawnerLogic().setEntityNBT(entityNBT);
    }

    private static void writeItemToNbtList(Pair<ItemStack, Float> item, NBTTagList equipment, NBTTagList dropChance) {
        if( item != null && item.getValue0() != null && item.getValue1() != null ) {
            NBTTagCompound nbt = new NBTTagCompound();
            item.getValue0().writeToNBT(nbt);
            equipment.appendTag(nbt);
            dropChance.appendTag(new NBTTagFloat(item.getValue1()));
        } else {
            dropChance.appendTag(new NBTTagFloat(0.0F));
        }
    }
}
