package com.zachg.economy.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

/**
 * WorldSavedData implementation for storing player economy balances.
 * Data is automatically saved when modified and persists across server restarts.
 */
public class EconomyData extends WorldSavedData {

    private static final String DATA_NAME = "EconomyData";
    private final Map<UUID, Double> balances = new ConcurrentHashMap<UUID, Double>();

    public EconomyData() {
        super(DATA_NAME);
    }

    public EconomyData(String name) {
        super(name);
    }

    /**
     * Get or create the EconomyData instance for the given world.
     */
    public static EconomyData get(World world) {
        EconomyData data = (EconomyData) world.loadItemData(EconomyData.class, DATA_NAME);
        if (data == null) {
            data = new EconomyData();
            world.setItemData(DATA_NAME, data);
        }
        return data;
    }

    /**
     * Read data from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        balances.clear();
        NBTTagList list = nbt.getTagList("balances", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(entry.getString("uuid"));
            double balance = entry.getDouble("balance");
            balances.put(uuid, balance);
        }
    }

    /**
     * Write data to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("uuid", entry.getKey().toString());
            compound.setDouble("balance", entry.getValue());
            list.appendTag(compound);
        }
        nbt.setTag("balances", list);
    }

    /**
     * Get the balance for a player UUID.
     */
    public double getBalance(UUID playerUUID) {
        if (playerUUID == null) {
            return 0.0;
        }
        Double balance = balances.get(playerUUID);
        return balance != null ? balance : 0.0;
    }

    /**
     * Set the balance for a player UUID.
     */
    public void setBalance(UUID playerUUID, double amount) {
        if (playerUUID == null) {
            return;
        }
        if (amount < 0) {
            amount = 0;
        }
        balances.put(playerUUID, amount);
        markDirty();
    }

    /**
     * Add to a player's balance.
     *
     * @param playerUUID The UUID of the player
     * @param amount     The amount to add
     * @return true if successful, false otherwise
     */
    public boolean addBalance(UUID playerUUID, double amount) {
        if (playerUUID == null || amount < 0) {
            return false;
        }
        double current = getBalance(playerUUID);
        setBalance(playerUUID, current + amount);
        return true;
    }

    /**
     * Remove from a player's balance.
     *
     * @param playerUUID The UUID of the player
     * @param amount     The amount to remove
     * @return true if sufficient balance, false otherwise
     */
    public boolean removeBalance(UUID playerUUID, double amount) {
        if (playerUUID == null || amount < 0) {
            return false;
        }
        double current = getBalance(playerUUID);
        if (current < amount) {
            return false;
        }
        setBalance(playerUUID, current - amount);
        return true;
    }
}
