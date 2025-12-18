package com.zachg.economy.api;

import java.util.UUID;

/**
 * Public API for accessing the economy system.
 * This interface provides methods for managing player balances.
 */
public interface IEconomyAPI {

    /**
     * Get the balance of a player.
     * 
     * @param playerUUID The UUID of the player
     * @return The player's balance, or 0 if the player has no balance data
     */
    double getBalance(UUID playerUUID);

    /**
     * Add to a player's balance.
     * 
     * @param playerUUID The UUID of the player
     * @param amount The amount to add (must be non-negative)
     * @return true if successful, false otherwise
     */
    boolean addBalance(UUID playerUUID, double amount);

    /**
     * Remove from a player's balance.
     * 
     * @param playerUUID The UUID of the player
     * @param amount The amount to remove (must be non-negative)
     * @return true if successful (player had sufficient balance), false otherwise
     */
    boolean removeBalance(UUID playerUUID, double amount);
}
