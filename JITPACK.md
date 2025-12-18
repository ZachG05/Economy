# JitPack Integration Guide

This document explains how the Economy mod is automatically published to JitPack and how other developers can use it as a dependency.

## 🚀 What is JitPack?

JitPack is a package repository for Git projects. It automatically builds and publishes your project when you create a GitHub release, making it easy for other developers to use your mod as a dependency in their projects.

**JitPack URL:** https://jitpack.io/#ZachG05/Economy

## 📦 How It Works

### Automatic Publishing

When you create a release:

1. **Create a Git tag:**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. **GitHub Actions automatically:**
   - Builds the mod with ForgeGradle
   - Creates a GitHub Release with the JAR file
   - Triggers JitPack to build and publish

3. **JitPack:**
   - Detects the new release
   - Clones your repository
   - Runs the build using `jitpack.yml` configuration
   - Publishes to Maven repository
   - Makes it available at: `com.github.ZachG05:Economy:v1.0.0`

### No Manual Upload Needed!

Everything is automated. You just need to create a Git tag and push it.

## 👨‍💻 For Mod Developers: Using Economy as a Dependency

### Gradle (Recommended)

Add to your `build.gradle`:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Use the latest release version
    implementation 'com.github.ZachG05:Economy:v1.0.0'
    
    // Or use a specific commit
    // implementation 'com.github.ZachG05:Economy:abc1234'
    
    // Or use the latest commit from a branch
    // implementation 'com.github.ZachG05:Economy:main-SNAPSHOT'
}
```

### Maven

Add to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.ZachG05</groupId>
        <artifactId>Economy</artifactId>
        <version>v1.0.0</version>
    </dependency>
</dependencies>
```

### Version Options

JitPack supports multiple version formats:

| Format | Example | Description |
|--------|---------|-------------|
| **Release Tag** | `v1.0.0` | Specific release version (recommended) |
| **Commit Hash** | `abc1234` | Specific commit |
| **Branch** | `main-SNAPSHOT` | Latest commit from branch |
| **Latest** | `latest` | Latest release (not recommended) |

## 🏗️ Configuration Files

### jitpack.yml

Located in the root of the repository, this file tells JitPack how to build your project:

```yaml
jdk:
  - openjdk8

before_install:
  - cp build-forge.gradle build.gradle
  - chmod +x gradlew

install:
  - ./gradlew clean setupDecompWorkspace --refresh-dependencies

after_success:
  - ./gradlew build publishToMavenLocal
```

### build-forge.gradle

The build file includes the `maven-publish` plugin configuration:

```gradle
apply plugin: 'maven-publish'

publishing {
    publications {
        mavenJava(MavenPublication) {
            groupId = 'com.zachg.economy'
            artifactId = 'economy'
            version = '1.0.0'
            
            from components.java
            artifact jar
        }
    }
}
```

## 📋 Release Process

### Step-by-Step: Creating a New Release

1. **Update version in build files:**
   ```gradle
   // In build-forge.gradle
   version = "1.0.1"
   ```

2. **Commit your changes:**
   ```bash
   git add .
   git commit -m "Release v1.0.1"
   git push
   ```

3. **Create and push tag:**
   ```bash
   git tag v1.0.1
   git push origin v1.0.1
   ```

4. **GitHub Actions will automatically:**
   - Build the mod
   - Create GitHub Release
   - Attach JAR file
   - Trigger JitPack

5. **Verify on JitPack:**
   - Visit: https://jitpack.io/#ZachG05/Economy
   - Check that your version appears with a green checkmark ✅

### Version Numbering

Follow Semantic Versioning (SemVer):
- **Major.Minor.Patch** (e.g., `v1.0.0`)
- **Major:** Breaking changes
- **Minor:** New features (backward compatible)
- **Patch:** Bug fixes

Examples:
- `v1.0.0` - Initial release
- `v1.0.1` - Bug fix
- `v1.1.0` - New feature
- `v2.0.0` - Breaking changes

## 🔍 Checking Build Status

### JitPack Dashboard

Visit: https://jitpack.io/#ZachG05/Economy

You'll see:
- ✅ Green checkmark: Build successful
- ❌ Red X: Build failed
- 🔄 Building: In progress

### Build Logs

Click on the version to see detailed build logs:
```
https://jitpack.io/com/github/ZachG05/Economy/v1.0.0/build.log
```

### Common Issues

**Build Failed?**
1. Check the build log on JitPack
2. Verify `jitpack.yml` configuration
3. Test build locally with same commands
4. Ensure ForgeGradle dependencies are accessible

## 📖 Using the Economy API

After adding the dependency, use the API in your mod:

```java
import com.zachg.economy.api.EconomyAPI;
import java.util.UUID;

@Mod(
    modid = "yourmod",
    dependencies = "after:economy"
)
public class YourMod {
    
    public void chargePlayer(UUID playerUUID, double amount) {
        if (EconomyAPI.isInitialized()) {
            boolean success = EconomyAPI.removeBalance(playerUUID, amount);
            if (success) {
                // Payment successful
            }
        }
    }
}
```

See [INTEGRATION.md](INTEGRATION.md) for complete API documentation.

## 🎯 Benefits of JitPack

✅ **Automatic Publishing** - No manual upload to Maven Central
✅ **Version Control** - Tied directly to Git tags
✅ **Easy Integration** - Simple URL and coordinates
✅ **Free for Open Source** - No cost
✅ **Build Logs** - Public build logs for transparency
✅ **Multiple Versions** - All releases available
✅ **Snapshot Support** - Can use branch commits

## 🔐 Security

- JitPack builds are reproducible (uses your source code)
- Build logs are publicly available
- No credentials needed (uses public GitHub repository)
- Builds run in isolated containers

## 📚 Additional Resources

- **JitPack Documentation:** https://jitpack.io/docs/
- **JitPack Dashboard:** https://jitpack.io/#ZachG05/Economy
- **GitHub Releases:** https://github.com/ZachG05/Economy/releases
- **Build Status Badge:** 
  ```markdown
  [![](https://jitpack.io/v/ZachG05/Economy.svg)](https://jitpack.io/#ZachG05/Economy)
  ```

## 🆘 Troubleshooting

### JitPack Build Fails

1. Check build log on JitPack
2. Verify Java 8 is used
3. Test locally: `cp build-forge.gradle build.gradle && ./gradlew setupDecompWorkspace && ./gradlew build`
4. Check ForgeGradle repository is accessible

### Cannot Resolve Dependency

1. Verify `jitpack.io` is in repositories
2. Check version tag exists: https://github.com/ZachG05/Economy/tags
3. Wait 1-2 minutes for JitPack first build
4. Clear Gradle cache: `./gradlew clean --refresh-dependencies`

### Wrong Version Downloaded

1. Clear Gradle cache: `~/.gradle/caches/`
2. Use `--refresh-dependencies` flag
3. Verify correct version in build file

## 💡 Tips

- Always test locally before creating a release
- Use release tags (v1.0.0) for stable versions
- Use branch-SNAPSHOT for development
- Add JitPack badge to README for visibility
- Keep version numbers consistent across files

---

## Summary

Your Economy mod is now set up for **automatic publishing to JitPack**! Simply create and push a Git tag, and GitHub Actions + JitPack handle the rest.

Other developers can now easily add your Economy mod as a dependency using standard Maven/Gradle coordinates.
