# Economy Mod - Build Requirements

This document explains everything you need to successfully build the Economy mod for Minecraft 1.7.10 with Forge.

## ✅ What You Currently Have

Your repository already contains:
- ✅ Complete Java source code for the Economy mod
- ✅ Gradle build scripts (both simplified and ForgeGradle versions)
- ✅ GitHub Actions workflow for automated builds
- ✅ Proper project structure and documentation

## 🔧 What's Needed to Build

### Option 1: Automated Build via GitHub Actions (RECOMMENDED)

**This is the easiest method and works automatically when you push code to GitHub.**

GitHub Actions has internet access and can download all required dependencies automatically. The build happens on GitHub's servers, and you can download the compiled JAR from the Actions tab.

**Setup:**
1. Push your code to GitHub (already configured)
2. GitHub Actions will automatically:
   - Download ForgeGradle 1.2
   - Download Minecraft 1.7.10 and Forge dependencies
   - Set up the decompiled workspace
   - Compile your mod
   - Upload the JAR as an artifact

**To get your built JAR:**
1. Go to your repository on GitHub
2. Click the "Actions" tab
3. Click on the latest successful workflow run
4. Scroll down to "Artifacts" section
5. Download "economy-mod" artifact (contains the compiled JAR)

**Current Status:** The GitHub Actions workflow is configured but needs updates to ensure successful ForgeGradle builds.

### Option 2: Local Development Build

To build locally on your own computer, you need:

#### Prerequisites
1. **Java Development Kit (JDK) 8**
   - Download from: https://adoptium.net/temurin/releases/?version=8
   - Must be JDK 8 (1.8.x), not newer versions
   - Verify: `java -version` should show "1.8.x"

2. **Internet Access**
   - Required to download ForgeGradle from maven.minecraftforge.net
   - Required to download Minecraft and Forge dependencies
   - Approximately 100-200 MB of downloads on first build

3. **Gradle** (included via gradlew wrapper)
   - No separate installation needed
   - Uses `./gradlew` (Linux/Mac) or `gradlew.bat` (Windows)

#### Build Steps

1. **Use the ForgeGradle build file:**
   ```bash
   cp build-forge.gradle build.gradle
   ```

2. **Set up the decompiled workspace (first time only):**
   ```bash
   ./gradlew setupDecompWorkspace --no-daemon
   ```
   This will:
   - Download Minecraft 1.7.10
   - Download Forge 10.13.4.1614
   - Decompile Minecraft source code
   - Set up the development environment
   - Takes 5-15 minutes on first run

3. **Build the mod:**
   ```bash
   ./gradlew build --no-daemon
   ```

4. **Find your JAR:**
   - Located in: `build/libs/economy-1.0.0.jar`
   - This is the complete, ready-to-use mod file

## 🚫 What You DON'T Need to Add Manually

**You do NOT need to manually add:**
- ❌ Minecraft 1.7.10 JAR file
- ❌ Forge JAR files
- ❌ Minecraft JSON files
- ❌ Any library dependencies

**Why?** ForgeGradle automatically downloads all of these from official sources when you run the build.

## 📁 About the Files in build/libs/

The repository includes two Minecraft-related files in `build/libs/`:

- **1.7.10.jar** - Vanilla Minecraft 1.7.10 client JAR (obfuscated, 5.1 MB)
- **1.7.10.json** - Minecraft 1.7.10 version manifest with library information

### What are these files?

These are reference files for Minecraft 1.7.10:
- The JAR contains the obfuscated Minecraft client code
- The JSON lists all required Minecraft libraries and their download URLs

### Do you need them to build?

**NO** - ForgeGradle automatically downloads these files when needed:
- During `setupDecompWorkspace`, ForgeGradle downloads Minecraft
- It then decompiles and patches the code for mod development
- The files go into your Gradle cache (`~/.gradle/caches/minecraft/`)

### Why are they in the repository?

These files can serve as:
- **Reference documentation** - Shows what libraries Minecraft 1.7.10 uses
- **Offline backup** - Could theoretically be used for offline builds (advanced setup)
- **Version verification** - Confirms exact Minecraft version being targeted

**For normal building:** You can safely ignore these files. ForgeGradle handles everything automatically.

## 🔍 Current Build Issues

When building WITHOUT ForgeGradle setup, you get these errors:
```
error: package net.minecraft.world does not exist
error: package cpw.mods.fml.common does not exist
error: package org.apache.logging.log4j does not exist
```

**Why?** The simplified `build.gradle` is designed for syntax checking only. It doesn't include Minecraft or Forge dependencies.

**Solution:** Use the ForgeGradle build process (Option 1 or Option 2 above).

## ✅ Making It Work with GitHub Actions

Your GitHub Actions workflow is already configured with two jobs:

1. **syntax-check** - Basic Java syntax validation (always runs)
2. **forge-build** - Full ForgeGradle build (attempts full build)

To ensure successful automated builds, the workflow needs:
- Proper gradle caching to speed up builds
- Correct ForgeGradle setup commands
- Proper artifact uploading

## 🎯 Summary - What You Need to Do

### For Local Building:
1. Install JDK 8
2. Have internet access
3. Run: `cp build-forge.gradle build.gradle`
4. Run: `./gradlew setupDecompWorkspace`
5. Run: `./gradlew build`
6. Get JAR from: `build/libs/economy-1.0.0.jar`

### For GitHub Actions (Automated):
1. Push your code to GitHub
2. Wait for Actions to complete
3. Download JAR artifact from Actions tab
4. (Improvements to workflow recommended - see below)

## 🔧 Recommended Improvements

I can help you with:

1. **Update GitHub Actions workflow** to ensure reliable ForgeGradle builds
2. **Add settings.gradle** for proper Gradle project configuration
3. **Improve caching** to make builds faster
4. **Add build status badges** to README
5. **Create release automation** to automatically create GitHub releases with JARs

Would you like me to implement these improvements?

## 🐛 Troubleshooting

### "Cannot resolve ForgeGradle"
- **Cause:** No internet access to maven.minecraftforge.net
- **Solution:** Ensure internet connection, check firewall/proxy settings

### "Wrong Java Version"
- **Cause:** Using Java 9+ instead of Java 8
- **Solution:** Install JDK 8 and set JAVA_HOME

### "Gradle Daemon Issues"
- **Cause:** Gradle daemon stuck or out of memory
- **Solution:** Run with `--no-daemon` flag

### "Access Denied to build/libs"
- **Cause:** Files locked or permissions issue
- **Solution:** Delete build/ directory and rebuild

## 📚 Additional Resources

- Forge 1.7.10 Documentation: http://mcforge.readthedocs.io/en/1.7.10/
- ForgeGradle Documentation: https://forgegradle.readthedocs.io/
- Gradle Documentation: https://docs.gradle.org/

## 🎉 What Happens After Successful Build

Once built, you'll have `economy-1.0.0.jar` which:
- Can be placed in any Minecraft 1.7.10 Forge server's `mods/` folder
- Will automatically initialize the economy system
- Provides the EconomyAPI for other mods to use
- Stores player balances in `world/data/EconomyData.dat`

Other mods can then use your Economy API without needing to build it - they just need the JAR in the mods folder.
