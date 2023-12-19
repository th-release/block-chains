package com.threlease;

import com.threlease.blockchain.Block;
import com.threlease.blockchain.Chain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Chain chain = new Chain();
        chain.addBlock(List.of("Block #1"));
        System.out.println(chain.getChain());
    }
}