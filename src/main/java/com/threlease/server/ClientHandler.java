package com.threlease.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("클라이언트 (" + clientSocket.getInetAddress().getHostAddress() + "): " + inputLine);
//                TODO: 소켓 관련 소스코드 작성
                out.println("서버 응답: " + inputLine);
            }
        } catch (IOException e) {
            System.out.println("클라이언트와의 통신 중 예외가 발생했습니다: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // 클라이언트 소켓을 닫습니다.
            } catch (IOException e) {
                System.out.println("클라이언트 소켓을 닫는 중 오류 발생: " + e.getMessage());
            }
        }
    }
}
