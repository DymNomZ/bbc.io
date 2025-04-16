package server;

import configs.SocketConfig;
import datas.AuthData;
import datas.InputData;
import server.model.PlayerData;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.List;

public class Lobby {
    private HashMap<InetAddress, PlayerData> players_data = new HashMap<>();
    static private int ID = 1;
    static private final int MAX_PLAYERS = 20;
    private int id;
    private DatagramSocket inputSocket;
    private boolean running = true;
    private final Thread game_thread, input_thread;

    public Lobby() throws IOException {
        // setup of maps
        do {
            id = ID++;
        } while (id == 0);

        inputSocket = new DatagramSocket(SocketConfig.PORT);
        inputSocket.setSoTimeout(3000);

        input_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                inputThread();
            }
        });
        game_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                gameThread();
            }
        });

        input_thread.start();
        game_thread.start();
    }

    private void gameThread() {
        // Game Algo
    }

    private void inputThread() {
        byte[] buf = new byte[9];
        DatagramPacket packet = new DatagramPacket(buf, 9);

        while (running) {
            try {
                inputSocket.receive(packet);
            } catch (IOException e) {
                continue;
            }

            PlayerData data = players_data.get(packet.getAddress());

            if (packet.getLength() != 9 || data == null) {
                continue;
            }

            data.setInputs(new InputData(packet.getData()));
        }
    }

    public boolean addPlayer(Socket client) throws IOException {
        if (players_data.size() < MAX_PLAYERS) {
            InputStream stream = client.getInputStream();
            AuthData authPacket = new AuthData(stream);

            // get data and extract
            PlayerData player = new PlayerData(authPacket, client, this);
            players_data.put(client.getInetAddress(), player);
        }

        return false;
    }
}
