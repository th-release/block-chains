package com.threlease;

import com.threlease.blockchain.Block;

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
        List<String> data = List.of(
                "cth Genesis"
        );

        Block block = new Block(null, List.of("cth Genesis"));

        System.out.println(block.toString());
    }
}