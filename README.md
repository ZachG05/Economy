# Economy

[![JitPack](https://jitpack.io/v/ZachG05/Economy.svg)](https://jitpack.io/#ZachG05/Economy)
[![Build](https://github.com/ZachG05/Economy/actions/workflows/build.yml/badge.svg)](https://github.com/ZachG05/Economy/actions/workflows/build.yml)

A server-side only economy system for Minecraft 1.7.10 with Forge. This mod provides a self-contained balance API that other mods (like auction houses) can easily integrate with.

## Features

- **Server-side only**: No client classes or GUIs
- **Self-contained**: Uses WorldSavedData for persistent storage, no external dependencies
- **Clean public API**: Simple interface for getting, adding, and removing balances
- **Safe handling**: Automatic handling of missing player data, world reloads, and server restarts
- **Early loading**: Initializes before other mods to ensure availability

## For Mod Developers: Using the Economy API

### Adding Economy as a Dependency

**Via JitPack (Recommended):**

Add JitPack repository and the dependency to your `build.gradle`:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ZachG05:Economy:v1.0.0'
}
```

**Or** add it as a soft dependency in your `@Mod` annotation:

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

See [REQUIREMENTS.md](REQUIREMENTS.md) for detailed build instructions.

**Quick build:**
```bash
cp build-forge.gradle build.gradle
./gradlew setupDecompWorkspace
./gradlew build
```

The compiled mod JAR will be in `build/libs/`.

## Releases and Distribution

This mod is automatically published to JitPack when a new release is created:

1. **GitHub Releases:** Download pre-built JARs from [Releases](https://github.com/ZachG05/Economy/releases)
2. **JitPack:** Use as a Maven/Gradle dependency via [JitPack.io](https://jitpack.io/#ZachG05/Economy)
3. **GitHub Actions:** Every push triggers an automated build

See [JITPACK.md](JITPACK.md) for details on the automatic publishing process.

## Documentation

- **[REQUIREMENTS.md](REQUIREMENTS.md)** - Build requirements and setup guide
- **[BUILD.md](BUILD.md)** - Detailed build instructions
- **[INTEGRATION.md](INTEGRATION.md)** - Complete API integration guide with examples
- **[JITPACK.md](JITPACK.md)** - JitPack publishing and dependency usage guide
- **[SUMMARY.md](SUMMARY.md)** - Implementation summary

## License

This project uses the same license as the GTNewHorizons ExampleMod1.7.10 template.