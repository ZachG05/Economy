# Building the Economy Mod

## Prerequisites

- Java JDK 8 (or Java 17/21 with modern Java syntax enabled)
- Gradle 9.2.0 (bundled via gradle wrapper)
- Internet connection for downloading dependencies

## Standard Build Process (GTNH Gradle System)

This project uses the [GTNH Gradle build system](https://github.com/GTNewHorizons/GTNHGradle), which provides a standardized build configuration for Minecraft 1.7.10 Forge mods.

1. **Set up the development workspace:**
   ```bash
   ./gradlew setupDecompWorkspace
   ```
   or for faster CI builds:
   ```bash
   ./gradlew setupCIWorkspace
   ```

2. **Build the mod:**
   ```bash
   ./gradlew build
   ```

3. The compiled mod JAR will be in `build/libs/`

## Development Setup

1. **Import into your IDE:**
   - IntelliJ IDEA: Import as Gradle project
   - Eclipse: Run `./gradlew eclipse` then import as existing project

2. **Run the development server:**
   ```bash
   ./gradlew runServer
   ```

3. **Run the development client (for testing):**
   ```bash
   ./gradlew runClient
   ```

## Project Structure

```
src/main/java/com/zachg/economy/
├── Economy.java              # Main mod class
├── EconomyImpl.java          # Economy API implementation
├── CommonProxy.java          # Server-side proxy
├── Tags.java                 # Version information
├── api/
│   ├── IEconomyAPI.java     # Public API interface
│   └── EconomyAPI.java      # Static API accessor
└── data/
    └── EconomyData.java     # WorldSavedData for balance storage

src/main/resources/
└── mcmod.info               # Mod metadata
```

## Configuration

The build is configured via `gradle.properties`. Key settings include:

- `modId`: The mod identifier (economy)
- `modName`: Human-readable mod name (Economy)
- `modGroup`: Root package (com.zachg.economy)
- `minecraftVersion`: Target Minecraft version (1.7.10)
- `forgeVersion`: Target Forge version (10.13.4.1614)

## Troubleshooting

### Cannot resolve GTNH repositories

Ensure you have internet access to `nexus.gtnewhorizons.com`. Some networks may block this repository.

### Wrong Java Version

The build supports Java 8, 17, and 21. Ensure you have a compatible JDK installed:
```bash
java -version
```

### Gradle Daemon Issues

If you encounter daemon issues, try:
```bash
./gradlew build --no-daemon
```

### First Build is Slow

The first build downloads many dependencies and sets up the Minecraft workspace. Subsequent builds will be much faster.

## Testing the Mod

1. Build the mod
2. Copy `build/libs/economy-1.0.0.jar` to your Minecraft 1.7.10 server's `mods/` folder
3. Start the server
4. The economy system will initialize automatically
5. Other mods can now access the economy API

## API Usage Example

```java
import com.zachg.economy.api.EconomyAPI;
import java.util.UUID;

// Check if Economy is loaded
if (EconomyAPI.isInitialized()) {
    UUID playerUUID = player.getUniqueID();
    
    // Get balance
    double balance = EconomyAPI.getBalance(playerUUID);
    
    // Add money
    EconomyAPI.addBalance(playerUUID, 100.0);
    
    // Remove money (returns false if insufficient funds)
    boolean success = EconomyAPI.removeBalance(playerUUID, 50.0);
}
```
