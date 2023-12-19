package com.threlease.blockchain;

import lombok.Getter;

import java.util.Date;

@Getter
public class BlockHeader {
    public String version;
    public long height;
    public long timestamp;
    public String previousHash;

    public BlockHeader(Block _previousBlock) {
        this.version = getVersion();
        this.height = (_previousBlock == null) ? 0 : _previousBlock.getHeight() + 1;
        this.timestamp = getTimestamp();
        this.previousHash = (_previousBlock == null || _previousBlock.getHash().isEmpty())
                ? "0".repeat(64)
                : _previousBlock.getHash();
    }

    public static String getVersion() {
        return "1.0.0";
    }

    public static long getTimestamp() {
        return new Date().getTime();
    }

    @Override
    public String toString() {
        return "BlockHeader {\n" +
                "version='" + version + '\'' + ",\n" +
                "height=" + height + ",\n" +
                "timestamp=" + timestamp + ",\n" +
                "previousHash='" + previousHash + '\'' + ",\n" +
                '}';
    }
}
