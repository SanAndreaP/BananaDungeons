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
    private String entityName;

    private Pair<ItemStack, Float> helmet;
    private Pair<ItemStack, Float> chestplate;
    private Pair<ItemStack, Float> leggings;
    private Pair<ItemStack, Float> boots;
    private Pair<ItemStack, Float> weapon;

    private List<PotionEffect> potions = new ArrayList<>(0);

    private SpawnerEntry(String entityName) {
        this.entityName = entityName;
    }

    public static SpawnerEntry newEntry(String entityName) {
        return new SpawnerEntry(entityName);
    }

    public SpawnerEntry addHelmet(ItemStack item, float dropChance) {
        this.helmet = Pair.with(item, dropChance);
        return this;
    }

    public SpawnerEntry addChestplate(ItemStack item, float dropChance) {
        this.chestplate = Pair.with(item, dropChance);
        return this;
    }

    public SpawnerEntry addLeggings(ItemStack item, float dropChance) {
        this.leggings = Pair.with(item, dropChance);
        return this;
    }

    public SpawnerEntry addBoots(ItemStack item, float dropChance) {
        this.boots = Pair.with(item, dropChance);
        return this;
    }

    public SpawnerEntry addHeldItem(ItemStack item, float dropChance) {
        this.weapon = Pair.with(item, dropChance);
        return this;
    }

    public SpawnerEntry addPotionEffect(int id, int duration, int amplifier) {
        this.potions.add(new PotionEffect(id, duration, amplifier));
        return this;
    }

    public void setupSpawner(TileEntityDungeonMobSpawner spawner) {
        NBTTagCompound entityNBT = new NBTTagCompound();
        NBTTagList equipment = new NBTTagList();
        NBTTagList dropChance = new NBTTagList();
        NBTTagList potionEffects = new NBTTagList();

        writeItemToNbtList(this.weapon, equipment, dropChance);
        writeItemToNbtList(this.boots, equipment, dropChance);
        writeItemToNbtList(this.leggings, equipment, dropChance);
        writeItemToNbtList(this.chestplate, equipment, dropChance);
        writeItemToNbtList(this.helmet, equipment, dropChance);

        entityNBT.setTag("Equipment", equipment);
        entityNBT.setTag("DropChances", dropChance);

        for( PotionEffect effect : this.potions ) {
            potionEffects.appendTag(effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
        }

        spawner.getSpawnerLogic().setEntityNBT(entityNBT, this.entityName);
    }

    private static void writeItemToNbtList(Pair<ItemStack, Float> item, NBTTagList equipment, NBTTagList dropChance) {
        if( item != null && item.getValue0() != null && item.getValue1() != null ) {
            NBTTagCompound nbt = new NBTTagCompound();
            item.getValue0().writeToNBT(nbt);
            equipment.appendTag(nbt);
            dropChance.appendTag(new NBTTagFloat(item.getValue1()));
        } else {
            equipment.appendTag(new NBTTagCompound());
            dropChance.appendTag(new NBTTagFloat(0.0F));
        }
    }
}
