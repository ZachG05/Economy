# Building the Economy Mod

## Prerequisites

- Java JDK 8
- Gradle 2.14 or compatible version
- Access to maven.minecraftforge.net (for ForgeGradle)

## Standard Build Process (With ForgeGradle)

If you have access to Minecraft Forge repositories:

1. **Use the ForgeGradle build file:**
   ```bash
   cp build-forge.gradle build.gradle
   ```

2. **Set up the development workspace:**
   ```bash
   ./gradlew setupDecompWorkspace
   ```

3. **Build the mod:**
   ```bash
   ./gradlew build
   ```

4. The compiled mod JAR will be in `build/libs/economy-1.0.0.jar`

## Alternative Build (Syntax Check Only)

The default `build.gradle` is simplified for CI/CD environments where Forge repositories may not be accessible. It performs syntax checking but won't produce a functional mod JAR.

```bash
./gradlew build
```

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

## Troubleshooting

### Cannot resolve ForgeGradle

If you get errors about ForgeGradle not being found, ensure you have internet access to `maven.minecraftforge.net`. Some networks or CI environments may block this.

### Wrong Java Version

Ensure you're using Java 8 (JDK 1.8):
```bash
java -version
```

Should show version 1.8.x

### Gradle Daemon Issues

If you encounter daemon issues, try:
```bash
./gradlew build --no-daemon
```

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
