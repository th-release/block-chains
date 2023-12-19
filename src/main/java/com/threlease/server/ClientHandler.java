package com.threlease.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threlease.blockchain.Block;
import com.threlease.blockchain.Chain;
import com.threlease.utils.Failable;
import com.threlease.utils.SocketData;
import com.threlease.utils.SocketResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private Chain chain;
    private final List<Socket> sockets = new ArrayList<Socket>();

    public ClientHandler(Socket socket, Chain chain) throws NoSuchAlgorithmException {
        super();
        this.chain = chain;
        sockets.add(socket);
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            String json;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("클라이언트 (" + clientSocket.getInetAddress().getHostAddress() + "): " + inputLine);
                ObjectMapper objectMapper = new ObjectMapper();
                SocketData data = objectMapper.readValue(inputLine, SocketData.class);
                SocketResponse response = SocketResponse.builder().build();
                switch (data.getPrefix()) {
                    case "chains":
                        json = objectMapper.writeValueAsString(chain.getChain());
                        response = SocketResponse.builder()
                                .success(true)
                                .message(Optional.empty())
                                .data(Optional.ofNullable(json))
                                .build();

                        if (response.getData().isPresent())
                            out.println(response.toString());
                        break;
                    case "mineBlock":
                        Failable<Block, String> fab = chain.addBlock(List.of(data.getData().toString()));
                        if(fab.isError()) {
                            response = SocketResponse.builder()
                                    .success(false)
                                    .message(Optional.of("체굴 중 오류가 발생했습니다."))
                                    .data(Optional.empty())
                                    .build();
                        } else {
                            Block newBlock = fab.getValue();
                            json = objectMapper.writeValueAsString(newBlock);
                            response = SocketResponse.builder()
                                    .success(true)
                                    .message(Optional.empty())
                                    .data(Optional.ofNullable(json))
                                    .build();
                        }

                        out.println(response.toString());
                        break;
                    case "peers":
                        List<String> SocketList = sockets.stream().map(socket -> socket.getInetAddress() + ":" + socket.getPort()).toList();
                        json = objectMapper.writeValueAsString(SocketList);
                        response = SocketResponse.builder()
                                .success(true)
                                .message(Optional.empty())
                                .data(Optional.of(json))
                                .build();

                        out.println(response.toString());
                        break;
                    default:
                        out.println(response.toString());
                        break;
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
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
