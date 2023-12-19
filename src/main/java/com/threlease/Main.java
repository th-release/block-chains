package com.threlease;

import com.threlease.blockchain.Chain;
import com.threlease.server.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class Main {
    private static final int PORT = 6666;
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Chain chain = new Chain();
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("서버가 " + PORT + " 포트에서 수신 대기 중입니다...");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clientSocket, chain)).start();
                } catch (IOException | NoSuchAlgorithmException e) {
                    System.out.println("클라이언트 연결 수락 중 예외 발생: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("서버 소켓을 열 수 없습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }
}