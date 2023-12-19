package com.threlease.blockchain;

import com.threlease.Config;
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

    public Block(
            Block _previousBlock,
            List<String> _data,
            Block _adjustmentBlock
    ) throws NoSuchAlgorithmException {
        super(_previousBlock);

        if (_previousBlock == null) {
            // If _previousBlock is null, treat it as a genesis block.
            this.height = 0;
            this.previousHash = "0".repeat(64);
        }

        this.merkleRoot = Block.getMerkleRoot(_data);
        this.hash = Block.createBlockHash(this);
        this.nonce = 0;
        this.difficulty = Block.getDifficult(
                this,
                _adjustmentBlock,
                _previousBlock
        );
        this.data = _data;
    }

    public static Block getGenesis() throws NoSuchAlgorithmException {
        return new Block(null, List.of("[cth Genesis]"), null);
    }

    public static String getMerkleRoot(List<String> data) {
        MerkleTree merkleTree = new MerkleTree(data);
        return merkleTree.getMerkleRoot();
    }

    public static String createBlockHash(Block _block) throws NoSuchAlgorithmException {
        String value = _block.version + _block.timestamp + _block.height + _block.merkleRoot + _block.previousHash+_block.difficulty+_block.nonce;
        return StringUtil.applySha256(value);
    }

    public static Block generateBlock(
            Block _previousBlock,
            List<String> _data,
            Block _adjustmentBlock
    ) throws NoSuchAlgorithmException {
        Block generatedBlock = new Block(_previousBlock, _data, _adjustmentBlock);

        return findBlock(generatedBlock);
    }

    public static Block findBlock(Block _generatedBlock) throws NoSuchAlgorithmException {
        String hash;
        long nonce = 0;
        while (true) {
            nonce++;
            _generatedBlock.nonce = nonce;
            hash = Block.createBlockHash(_generatedBlock);
            String binary = StringUtil.hexToBinary(hash);
            boolean result = binary.startsWith("0".repeat((int) _generatedBlock.difficulty));

            if (result) {
                _generatedBlock.hash = hash;
                return _generatedBlock;
            }
        }
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

    public static long getDifficult(
            Block _newBlock,
            Block _adjustmentBlock,
            Block _previousBlock
    ) {
        if (_newBlock.height <= 9) return 0;
        if (_newBlock.height <= 19) return 1;

        if (_newBlock.height % Config.DIFFICULTY_ADJUSTMENT_INTERVAL != 0)
            return _previousBlock.difficulty;

        long timeTaken = _newBlock.timestamp - _adjustmentBlock.timestamp;
        long timeExpected = Config.BLOCK_GENERATION_TIME_UNIT *
                Config.BLOCK_GENERATION_INTERVAL *
                Config.DIFFICULTY_ADJUSTMENT_INTERVAL;

        if (timeTaken < timeExpected / 2)
            return _adjustmentBlock.difficulty + 1;
        else if (timeTaken > timeExpected / 2)
            return _adjustmentBlock.difficulty - 1;

        return _adjustmentBlock.difficulty;
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

