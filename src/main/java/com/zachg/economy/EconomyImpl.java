package com.zachg.economy;

import java.util.UUID;

import com.zachg.economy.api.IEconomyAPI;
import com.zachg.economy.data.EconomyData;

import net.minecraft.world.World;

/**
 * Internal implementation of the economy API.
 */
public class EconomyImpl implements IEconomyAPI {

    private World world;

    public EconomyImpl(World world) {
        this.world = world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public double getBalance(UUID playerUUID) {
        if (world == null || playerUUID == null) {
            return 0.0;
        }
        EconomyData data = EconomyData.get(world);
        return data.getBalance(playerUUID);
    }

    @Override
    public boolean addBalance(UUID playerUUID, double amount) {
        if (world == null || playerUUID == null || amount < 0) {
            return false;
        }
        EconomyData data = EconomyData.get(world);
        return data.addBalance(playerUUID, amount);
    }

    @Override
    public boolean removeBalance(UUID playerUUID, double amount) {
        if (world == null || playerUUID == null || amount < 0) {
            return false;
        }
        EconomyData data = EconomyData.get(world);
        return data.removeBalance(playerUUID, amount);
    }
}
