# Economy

A server-side only economy system for Minecraft 1.7.10 with Forge. This mod provides a self-contained balance API that other mods (like auction houses) can easily integrate with.

## Features

- **Server-side only**: No client classes or GUIs
- **Self-contained**: Uses WorldSavedData for persistent storage, no external dependencies
- **Clean public API**: Simple interface for getting, adding, and removing balances
- **Safe handling**: Automatic handling of missing player data, world reloads, and server restarts
- **Early loading**: Initializes before other mods to ensure availability

## For Mod Developers: Using the Economy API

### Adding Economy as a Dependency

#### Via JitPack

Add JitPack repository to your `repositories.gradle`:

```gradle
repositories {
    maven {
        name = "JitPack"
        url = "https://jitpack.io"
    }
}
```

Then add Economy to your `dependencies.gradle`:

```gradle
dependencies {
    implementation 'com.github.ZachG05:Economy:1.0.0'
}
```

Or if using the GTNH convention plugin, add to `dependencies.gradle`:

```gradle
dependencies {
    runtimeOnlyNonPublishable("com.github.ZachG05:Economy:1.0.0")
}
```

Or add it as a soft dependency in your `@Mod` annotation:

```java
@Mod(
    modid = "yourmod",
    dependencies = "after:economy"
)
```

### Using the API in Your Code

The Economy mod provides a simple static API for balance management:

```java
import com.zachg.economy.api.EconomyAPI;
import java.util.UUID;

public class YourAuctionHouse {
    
    public void chargeFee(UUID playerUUID, double amount) {
        // Check if Economy is loaded
        if (!EconomyAPI.isInitialized()) {
            // Handle Economy not being available
            return;
        }
        
        // Check if player has enough balance
        double balance = EconomyAPI.getBalance(playerUUID);
        if (balance >= amount) {
            // Remove the fee from player's balance
            boolean success = EconomyAPI.removeBalance(playerUUID, amount);
            if (success) {
                // Fee charged successfully
            }
        }
    }
    
    public void payPlayer(UUID playerUUID, double amount) {
        // Add money to player's balance
        boolean success = EconomyAPI.addBalance(playerUUID, amount);
        if (success) {
            // Payment successful
        }
    }
    
    public double getPlayerBalance(UUID playerUUID) {
        // Get player's current balance
        return EconomyAPI.getBalance(playerUUID);
    }
}
```

### API Methods

- **`EconomyAPI.isInitialized()`**: Check if the economy system is loaded and ready
- **`EconomyAPI.getBalance(UUID playerUUID)`**: Get a player's current balance (returns 0 if no data)
- **`EconomyAPI.addBalance(UUID playerUUID, double amount)`**: Add to a player's balance (returns true on success)
- **`EconomyAPI.removeBalance(UUID playerUUID, double amount)`**: Remove from a player's balance (returns true if sufficient funds, false otherwise)

### Thread Safety

All economy operations are thread-safe when called from the server thread. The economy data is automatically saved when modified.

## Building

This project uses the GTNH Gradle build system. See [BUILD.md](BUILD.md) for detailed build instructions.

Quick start:
```bash
./gradlew setupCIWorkspace
./gradlew build
```

The compiled mod JAR will be in `build/libs/`.

## GitHub Actions & JitPack

This mod is configured to:
- Build automatically on GitHub Actions for every commit
- Publish releases to JitPack for easy dependency management
- Support tagged releases for versioning

## License

This project uses the same license as the GTNewHorizons ExampleMod1.7.10 template.