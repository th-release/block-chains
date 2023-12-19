package com.threlease;

import com.threlease.server.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int PORT = 6666;
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("서버가 " + PORT + " 포트에서 수신 대기 중입니다...");
            while (true) {
                try {
                    // 클라이언트 연결을 기다립니다.
                    Socket clientSocket = serverSocket.accept();
                    // 각 클라이언트 연결에 대해 새로운 스레드를 시작합니다.
                    new Thread(new ClientHandler(clientSocket)).start();
                } catch (IOException e) {
                    System.out.println("클라이언트 연결 수₩락 중 예외 발생: " + e.getMessage());
                    // 여기서 에러를 핸들링하고 루프를 계속합니다.
                }
            }
        } catch (IOException e) {
            System.out.println("서버 소켓을 열 수 없습니다: " + e.getMessage());
            e.printStackTrace();
        }

//        Chain chain = new Chain();
//        chain.addBlock(List.of("Block #01"));
//        System.out.println(chain.getChain());
    }
}