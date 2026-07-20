<p align="center">
  <img src="src/main/resources/rosetta_library_icon.png" alt="Rosetta Library icon" width="128" style="image-rendering: pixelated">
</p>

# Rosetta

Rosetta is a compatibility library for Minecraft mods that support multiple game versions and mod loaders from a shared codebase.

It provides compatibility helpers for registries, NBT, worlds, biomes, attributes, entities, textures, GUI rendering, gamerules, networking, and persistent level attachments. Platform utilities are available for Fabric, Forge, and NeoForge.

Rosetta uses [Stonecutter](https://stonecutter.kikugie.dev/) to maintain its version-specific implementations. Networking and persistent attachments use [Data Anchor](https://modrinth.com/mod/data-anchor), which is a required dependency.

## Support

| MC Version | Fabric Version | Forge Version | NeoForge Version | Quilt Version |
|:----------:|:--------------:|:-------------:|:----------------:|:-------------:|
|   26.1.x   |   ✅ `v1.0.0`   | ❌ Not Planned |    ✅ `v1.0.0`    | ❌ Not Planned |
|   1.21.1   |   ✅ `v1.0.0`   | ❌ Not Planned |    ✅ `v1.0.0`    | ❌ Not Planned |
|   1.20.1   |   ✅ `v1.0.0`   |  ✅ `v1.0.0`   |  ❌ Not Planned   | ❌ Not Planned |

## Building

Build every supported target:

```powershell
.\gradlew.bat buildReleaseArtifacts
```

Target-specific jars are written to `build/release`.

Build one target:

```powershell
.\gradlew.bat :1.21.1-fabric:build
```

Available targets are `26.1-fabric`, `26.1-neoforge`, `1.21.1-fabric`, `1.21.1-neoforge`, `1.20.1-fabric`, and `1.20.1-forge`.

## Usage

### Import

Maven coordinates will be documented when public Maven distribution is available.

### Persistent level attachments

```java
LevelAttachmentKey<MyData> DATA = RosettaAttachments.level("example_mod").persistent(
        "data",
        level -> new MyData(),
        (level, value) -> value.save(),
        (level, value, tag) -> value.load(tag)
);

MyData data = DATA.getOrCreate(level);
DATA.markDirty(level);
```

### Networking

Create a channel with `RosettaNetwork.channel(namespace)`, register packets with `serverbound` or `clientbound`, and send them with `RosettaNetwork.sendToServer` or `RosettaNetwork.sendToPlayer`.

## License

MIT
