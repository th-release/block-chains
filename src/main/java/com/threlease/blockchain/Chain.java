package com.threlease.blockchain;

import com.threlease.Config;
import com.threlease.utils.Failable;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Chain {
    private final List<Block> blockchain;

    public Chain() throws NoSuchAlgorithmException {
        this.blockchain = new ArrayList<>();

        blockchain.add(Block.getGenesis());
    }

    public List<Block> getChain() {
        return this.blockchain;
    }

    public long getLength() {
        return this.blockchain.size();
    }

    public Block getLatestBlock() {
        return this.blockchain.get(this.blockchain.size() - 1);
    }

    public Failable<Block, String> addBlock(List<String> data) throws NoSuchAlgorithmException {
        Block previousBlock = this.getLatestBlock();
        Block newBlock = Block.generateBlock(previousBlock, data, getAdjustmentBlock());
        Failable<Block, String> isValid = Block.isValidNewBlock(newBlock, previousBlock);

        if (isValid.isError())
                return Failable.error(isValid.getError());
        this.blockchain.add(newBlock);
        return Failable.success(newBlock);
    }

    /*
    * getAdjustmentBlock()
    * 생성 시점 기준으로 블록 높이가 -10 인 블록 구하기
    * 1. 현재 높이값 < DIFFICULTY_ADJUSTMENT_INTERVAL : 제네시스 블록 반환
    * 2. 현재 높이값 > DIFFICULTY_ADJUSTMENT_INTERVAL : -10번째 블록 반환
    */
    public Block getAdjustmentBlock() throws NoSuchAlgorithmException {
        return this.getLength() < Config.DIFFICULTY_ADJUSTMENT_INTERVAL
                ? Block.getGenesis()
                : this.blockchain.get((int) (this.getLength() - Config.DIFFICULTY_ADJUSTMENT_INTERVAL));
    }

}
