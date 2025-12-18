# Economy API Integration Guide

This guide shows how to integrate the Economy mod into your Minecraft 1.7.10 Forge mod (like an Auction House).

## Quick Start

### 1. Add Economy as a Soft Dependency

In your mod's main class, add Economy as an optional dependency:

```java
@Mod(
    modid = "yourmod",
    name = "Your Mod",
    version = "1.0",
    dependencies = "after:economy"  // Loads after Economy if present
)
public class YourMod {
    // Your mod code
}
```

### 2. Check if Economy is Available

```java
import com.zachg.economy.api.EconomyAPI;

public class YourModLogic {
    public boolean hasEconomySupport() {
        return EconomyAPI.isInitialized();
    }
}
```

### 3. Use the Economy API

```java
import com.zachg.economy.api.EconomyAPI;
import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;

public class AuctionHouse {
    
    // Charge a listing fee when a player creates an auction
    public boolean chargeListingFee(EntityPlayer player, double fee) {
        UUID playerUUID = player.getUniqueID();
        
        // Check if player has enough money
        double balance = EconomyAPI.getBalance(playerUUID);
        if (balance < fee) {
            player.addChatMessage("You need " + fee + " coins to list this item!");
            return false;
        }
        
        // Charge the fee
        boolean success = EconomyAPI.removeBalance(playerUUID, fee);
        if (success) {
            player.addChatMessage("Listing fee of " + fee + " coins charged.");
        }
        return success;
    }
    
    // Pay a seller when their item is sold
    public boolean paySeller(UUID sellerUUID, double amount) {
        boolean success = EconomyAPI.addBalance(sellerUUID, amount);
        return success;
    }
    
    // Check if a buyer can afford an item
    public boolean canAfford(UUID buyerUUID, double price) {
        double balance = EconomyAPI.getBalance(buyerUUID);
        return balance >= price;
    }
    
    // Complete a purchase transaction
    public boolean completePurchase(EntityPlayer buyer, UUID sellerUUID, double price) {
        UUID buyerUUID = buyer.getUniqueID();
        
        // Check buyer has funds
        if (!canAfford(buyerUUID, price)) {
            buyer.addChatMessage("Insufficient funds! You need " + price + " coins.");
            return false;
        }
        
        // Remove money from buyer
        if (!EconomyAPI.removeBalance(buyerUUID, price)) {
            buyer.addChatMessage("Transaction failed!");
            return false;
        }
        
        // Add money to seller
        if (!EconomyAPI.addBalance(sellerUUID, price)) {
            // Refund buyer if seller payment fails
            EconomyAPI.addBalance(buyerUUID, price);
            buyer.addChatMessage("Transaction failed!");
            return false;
        }
        
        buyer.addChatMessage("Purchase successful! " + price + " coins transferred.");
        return true;
    }
    
    // Display player's balance
    public void showBalance(EntityPlayer player) {
        UUID playerUUID = player.getUniqueID();
        double balance = EconomyAPI.getBalance(playerUUID);
        player.addChatMessage("Your balance: " + balance + " coins");
    }
}
```

## API Reference

### EconomyAPI Class

The main static accessor for economy functions.

#### Methods

**`static boolean isInitialized()`**
- Returns `true` if the Economy mod is loaded and the API is ready to use
- Always check this before using other API methods
- Returns `false` if Economy mod is not installed

**`static double getBalance(UUID playerUUID)`**
- Get the current balance of a player
- **Parameters:**
  - `playerUUID` - The player's unique ID
- **Returns:** The player's balance as a double, or `0.0` if the player has no balance data
- **Throws:** `IllegalStateException` if Economy is not initialized

**`static boolean addBalance(UUID playerUUID, double amount)`**
- Add money to a player's balance
- **Parameters:**
  - `playerUUID` - The player's unique ID
  - `amount` - The amount to add (must be >= 0)
- **Returns:** `true` if successful, `false` if invalid parameters
- **Throws:** `IllegalStateException` if Economy is not initialized

**`static boolean removeBalance(UUID playerUUID, double amount)`**
- Remove money from a player's balance
- **Parameters:**
  - `playerUUID` - The player's unique ID
  - `amount` - The amount to remove (must be >= 0)
- **Returns:** `true` if successful (player had sufficient funds), `false` if insufficient funds or invalid parameters
- **Throws:** `IllegalStateException` if Economy is not initialized

## Best Practices

### 1. Always Check Initialization

```java
if (!EconomyAPI.isInitialized()) {
    // Provide fallback behavior or inform the user
    player.addChatMessage("Economy system not available!");
    return;
}
```

### 2. Handle Transaction Failures

```java
boolean success = EconomyAPI.removeBalance(playerUUID, cost);
if (!success) {
    // Handle failure - insufficient funds or invalid parameters
    player.addChatMessage("Transaction failed!");
}
```

### 3. Use Transactions for Transfers

When transferring money between players, remove from one and add to the other. If the add fails, refund the first player:

```java
if (EconomyAPI.removeBalance(fromUUID, amount)) {
    if (!EconomyAPI.addBalance(toUUID, amount)) {
        // Refund if recipient add fails
        EconomyAPI.addBalance(fromUUID, amount);
        return false;
    }
    return true;
}
return false;
```

### 4. Validate UUIDs

```java
if (player.getUniqueID() == null) {
    // Handle invalid UUID
    return;
}
```

### 5. Format Currency Display

```java
import java.text.DecimalFormat;

DecimalFormat df = new DecimalFormat("#,##0.00");
String formatted = df.format(balance) + " coins";
```

## Thread Safety

- All Economy API calls are safe to make from the server thread
- Do not call Economy API from client-side code (the mod is server-only)
- The economy data is automatically saved when modified

## Data Persistence

- Player balances are stored using Minecraft's WorldSavedData system
- Data persists across server restarts
- Data is saved in the world save folder
- New players start with a balance of 0.0

## Error Handling

The Economy API uses the following error handling:

- Invalid parameters (null UUID, negative amounts) return `false`
- Insufficient funds returns `false`
- If Economy is not initialized, methods throw `IllegalStateException`
- Check `isInitialized()` first to avoid exceptions

## Examples for Common Use Cases

### Admin Commands to Give Money

```java
@Command(name = "givemoney")
public void giveMoneyCommand(EntityPlayer admin, EntityPlayer target, double amount) {
    if (!EconomyAPI.isInitialized()) {
        admin.addChatMessage("Economy system not available!");
        return;
    }
    
    UUID targetUUID = target.getUniqueID();
    if (EconomyAPI.addBalance(targetUUID, amount)) {
        admin.addChatMessage("Gave " + amount + " coins to " + target.getDisplayName());
        target.addChatMessage("You received " + amount + " coins!");
    }
}
```

### Shop Purchase System

```java
public boolean purchaseItem(EntityPlayer player, ItemStack item, double price) {
    if (!EconomyAPI.isInitialized()) {
        player.addChatMessage("Shop unavailable - Economy system offline");
        return false;
    }
    
    UUID playerUUID = player.getUniqueID();
    
    // Check balance
    if (EconomyAPI.getBalance(playerUUID) < price) {
        player.addChatMessage("You need " + price + " coins to buy this item");
        return false;
    }
    
    // Charge player
    if (EconomyAPI.removeBalance(playerUUID, price)) {
        // Give item to player
        player.inventory.addItemStackToInventory(item.copy());
        player.addChatMessage("Purchased " + item.getDisplayName() + " for " + price + " coins");
        return true;
    }
    
    return false;
}
```

### Reward System

```java
public void rewardPlayer(EntityPlayer player, double reward, String reason) {
    if (EconomyAPI.isInitialized()) {
        UUID playerUUID = player.getUniqueID();
        if (EconomyAPI.addBalance(playerUUID, reward)) {
            player.addChatMessage("+" + reward + " coins! " + reason);
        }
    }
}
```

## Support

For issues or questions about the Economy API:
- Check that Economy mod is installed on the server
- Verify Economy is loading before your mod (use `dependencies = "after:economy"`)
- Ensure you're checking `isInitialized()` before API calls
- Make sure you're calling the API from server-side code only

## Version Compatibility

- Economy Mod: 1.0.0+
- Minecraft: 1.7.10
- Forge: 10.13.4.1614+

This API is designed to be stable and backward-compatible. Future versions will maintain these method signatures.
