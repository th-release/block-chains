package com.threlease.blockchain;

import com.threlease.utils.Failable;
import com.threlease.utils.MerkleTree;
import lombok.Getter;
import com.threlease.utils.StringUtil;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Getter
public class Block extends BlockHeader {
    public String hash;
    public String merkleRoot;
    public long nonce;
    public long difficulty;
    public List<String> data;

    public Block(Block _previousBlock, List<String> _data) throws NoSuchAlgorithmException {
        super(_previousBlock);

        if (_previousBlock == null) {
            // If _previousBlock is null, treat it as a genesis block.
            this.height = 0;
            this.previousHash = "0".repeat(64);
        }

        this.merkleRoot = Block.getMerkleRoot(_data);
        this.hash = Block.createBlockHash(this);
        this.nonce = 0;
        this.difficulty = 0;
        this.data = _data;
    }

    static String getMerkleRoot(List<String> data) {
        MerkleTree merkleTree = new MerkleTree(data);
        return merkleTree.getMerkleRoot();
    }

    public static String createBlockHash(Block _block) throws NoSuchAlgorithmException {
        String value = _block.version + _block.timestamp + _block.height + _block.merkleRoot + _block.previousHash;
        return StringUtil.applySha256(value);
    }

    public static Block getGenesis() throws NoSuchAlgorithmException {
        return new Block(null, List.of("[cth Genesis]"));
    }

    public static Block generateBlock(Block _previousBlock, List<String> _data) throws NoSuchAlgorithmException {
        return new Block(_previousBlock, _data);
    }

    public static Failable<Block, String> isValidNewBlock(
            Block _newBlock,
            Block _previousBlock
    ) throws NoSuchAlgorithmException {
        if (_previousBlock.getHeight() + 1 != _newBlock.getHeight()) {
            return Failable.error("height error");
        }
        if (!_previousBlock.getHash().equals(_newBlock.getPreviousHash())) {
            return Failable.error("previousHash error");
        }
        if (!Block.createBlockHash(_newBlock).equals(_newBlock.getHash())) {
            return Failable.error("block hash error");
        }
        return Failable.success(_newBlock);
    }

    @Override
    public String toString() {
        return "Block {\n" +
                "version='" + version + '\'' + ",\n" +
                "height=" + height + "\n" +
                "timestamp=" + timestamp + ",\n" +
                "previousHash='" + previousHash + '\'' + ",\n" +
                "hash='" + hash + '\'' + ",\n" +
                "merkleRoot='" + merkleRoot + '\'' + ",\n" +
                "data=" + data + ",\n" +
                '}';
    }
}

