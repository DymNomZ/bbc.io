package server;

import configs.LobbyConfig;
import datas.*;
import server.model.PlayerData;
import server.model.ServerEntity;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.WeakHashMap;

public class Lobby {
    public HashMap<InetAddress, PlayerData> players_data = new HashMap<>();

    // TODO: May be replaced for quad trees data struct
    public WeakHashMap<PlayerData, ServerEntity> entity_data = new WeakHashMap<>();

    public LinkedList<PlayerData> spawn_queue = new LinkedList<>();
    static private int ID = 1;
    private int id;
    public final DatagramSocket input_socket;
    public boolean running = true;
    private final Thread game_thread, input_thread;

    public Lobby() throws IOException {
        // TODO: Initialize Map (if required)
        do {
            id = ID++;
        } while (id == 0);

        input_socket = new DatagramSocket();
        input_socket.setSoTimeout(3000);

        Logging.write(this, "UDP PORT: " + input_socket.getLocalPort());

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

    // TODO: GAME LOOP HERE, SETH
    private void gameThread() {
        while (running) {
            long game_clock = System.currentTimeMillis();

            for (PlayerData i : entity_data.keySet()) {
                ServerEntity entity = entity_data.get(i);

                if (entity.is_projectile) {
                    entity.move(game_clock);
                } else {
                    entity.move(game_clock, i.getInputs());
                }
            }

            while (!spawn_queue.isEmpty()) {
                PlayerData player = spawn_queue.remove();
                entity_data.put(player, new ServerEntity(player.id, 0, game_clock));
            }

            long sleep_clock = Math.max(2 - (System.currentTimeMillis() - game_clock), 0);
            try {
                Thread.sleep(sleep_clock);
            } catch (InterruptedException e) {
                running = false;
                return;
            }
        }
    }

    private void inputThread() {
        byte[] buf = new byte[9];
        DatagramPacket packet = new DatagramPacket(buf, 9);

        while (running) {
            try {
                input_socket.receive(packet);
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
        if (players_data.size() < LobbyConfig.MAX_PLAYERS) {
            InputStream stream = client.getInputStream();
            AuthData authPacket = new AuthData(stream);

            PlayerData player = new PlayerData(authPacket);
            players_data.put(client.getInetAddress(), player);
            player.startHandler(client, this);
            return true;
        }

        return false;
    }

    public LobbyData initialLobbyData() {
        LinkedList<UserData> users = new LinkedList<>();

        for (PlayerData i : players_data.values()) {
            users.add(new UserData(i));
            Logging.write(this, i.name);
        }

        return new LobbyData(id, users);
    }
}
