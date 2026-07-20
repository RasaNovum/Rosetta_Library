package net.rasanovum.rosetta.util;

/** World and chunk helpers. */
public final class WorldCompat {
    private WorldCompat() {}

    public static long chunkPosAsLong(int chunkX, int chunkZ) {
        //? if >=26.1 {
        /*return net.minecraft.world.level.ChunkPos.pack(chunkX, chunkZ);
        *///?} else {
        return net.minecraft.world.level.ChunkPos.asLong(chunkX, chunkZ);
        //?}
    }

    public static int chunkX(net.minecraft.world.level.ChunkPos pos) {
        //? if >=26.1 {
        /*return pos.x();
        *///?} else {
        return pos.x;
        //?}
    }

    public static int chunkZ(net.minecraft.world.level.ChunkPos pos) {
        //? if >=26.1 {
        /*return pos.z();
        *///?} else {
        return pos.z;
        //?}
    }

    public static long chunkPosToLong(net.minecraft.world.level.ChunkPos pos) {
        //? if >=26.1 {
        /*return pos.pack();
        *///?} else {
        return pos.toLong();
        //?}
    }

    public static int minBuildHeight(net.minecraft.world.level.LevelHeightAccessor level) {
        //? if >=26.1 {
        /*return level.getMinY();
        *///?} else {
        return level.getMinBuildHeight();
        //?}
    }

    public static net.minecraft.world.level.ChunkPos chunkPosFromBlockPos(net.minecraft.core.BlockPos pos) {
        //? if >=26.1 {
        /*return new net.minecraft.world.level.ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
        *///?} else {
        return new net.minecraft.world.level.ChunkPos(pos);
        //?}
    }

    public static int maxBuildHeight(net.minecraft.world.level.LevelHeightAccessor level) {
        //? if >=26.1 {
        /*return level.getMaxY() + 1;
        *///?} else {
        return level.getMaxBuildHeight();
        //?}
    }
}
