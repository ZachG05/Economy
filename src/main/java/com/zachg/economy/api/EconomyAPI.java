package com.zachg.economy.api;

import java.util.UUID;

/**
 * Static accessor for the economy API.
 * Use this class to access economy functions from other mods.
 */
public class EconomyAPI {

    private static IEconomyAPI instance;

    /**
     * Internal method to set the API instance. Do not call from external mods.
     */
    public static void setInstance(IEconomyAPI api) {
        instance = api;
    }

    /**
     * Get the balance of a player.
     * 
     * @param playerUUID The UUID of the player
     * @return The player's balance, or 0 if the player has no balance data
     */
    public static double getBalance(UUID playerUUID) {
        if (instance == null) {
            throw new IllegalStateException("Economy API not initialized");
        }
        return instance.getBalance(playerUUID);
    }

    /**
     * Add to a player's balance.
     * 
     * @param playerUUID The UUID of the player
     * @param amount The amount to add (must be non-negative)
     * @return true if successful, false otherwise
     */
    public static boolean addBalance(UUID playerUUID, double amount) {
        if (instance == null) {
            throw new IllegalStateException("Economy API not initialized");
        }
        return instance.addBalance(playerUUID, amount);
    }

    /**
     * Remove from a player's balance.
     * 
     * @param playerUUID The UUID of the player
     * @param amount The amount to remove (must be non-negative)
     * @return true if successful (player had sufficient balance), false otherwise
     */
    public static boolean removeBalance(UUID playerUUID, double amount) {
        if (instance == null) {
            throw new IllegalStateException("Economy API not initialized");
        }
        return instance.removeBalance(playerUUID, amount);
    }

    /**
     * Check if the economy API is initialized and ready to use.
     * 
     * @return true if the API is ready, false otherwise
     */
    public static boolean isInitialized() {
        return instance != null;
    }
}
