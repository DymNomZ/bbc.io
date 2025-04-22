package server;

import configs.DimensionConfig;
import configs.LobbyConfig;
import configs.SocketConfig;
import datas.*;
import server.model.*;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

import game_data.*;

public class Lobby {
    public HashMap<InetAddress, PlayerData> players_data = new HashMap<>();

    // TODO: May be replaced for quad trees data struct
    public WeakHashMap<PlayerData, ServerEntity> entity_data = new WeakHashMap<>();

    public LinkedList<PlayerData> spawn_queue = new LinkedList<>();
    static private int ID = 1;
    private int id;
    private int player_id_ctr = 1;
    QuadTree qtree;
    public final DatagramSocket input_socket;
    public boolean running = true;
    private final Thread game_thread, input_thread;

    public Lobby() throws IOException {
        // TODO: Initialize Map (if required)
        do {
            id = ID++;
        } while (id == 0);

        input_socket = new DatagramSocket(SocketConfig.PORT);
        input_socket.setSoTimeout(10);

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
        qtree = new QuadTree(new QuadRectangle(0,0, DimensionConfig.MAP_WIDTH, DimensionConfig.MAP_HEIGHT),1);
        input_thread.start();
        game_thread.start();
    }

    private void gameThread() {
        while (running) {
            long game_clock = System.currentTimeMillis();

            moveEntities(game_clock);

            handleSpawnQueue(game_clock);

            constructQTree();

            quadCollisionCheck();

            long sleep_clock = Math.max(2 - (System.currentTimeMillis() - game_clock), 0);
            try {
                Thread.sleep(sleep_clock);
            } catch (InterruptedException e) {
                running = false;
                return;
            }
        }
    }

    private void quadCollisionCheck(){
        for (ServerEntity entity: entity_data.values()) {
            RangeCircle circle = new RangeCircle(entity.x, entity.y, entity.radius + 1);
            List<ServerEntity> collision = qtree.query(circle);
            for (ServerEntity other : collision) {
                if (entity != other) entity.isCollidingWith(other);
            }
        }
    }

    private void constructQTree(){
        qtree.clear();
        for(ServerEntity s : entity_data.values()){
            qtree.insert(s);
        }
    }

    private void handleSpawnQueue(long game_clock){
        while (!spawn_queue.isEmpty()) {
            PlayerData player = spawn_queue.remove();
            entity_data.put(player, new PlayerEntity(game_clock,player.id));
        }
    }
    private void moveEntities(long game_clock){
        for (PlayerData i : entity_data.keySet()) {
            ServerEntity entity = entity_data.get(i);

            if (entity instanceof ProjectileEntity) {
                entity.move(game_clock);
            } else {
                entity.move(game_clock, i.getInputs());
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

        // TODO: find a way to implement multiple lobbies later
//        if (players_data.size() < LobbyConfig.MAX_PLAYERS) {
//            InputStream stream = client.getInputStream();
//            AuthData authPacket = new AuthData(stream);
//
//            PlayerData player = new PlayerData(authPacket, player_id_ctr++);
//            players_data.put(client.getInetAddress(), player);
//            player.startHandler(client, this);
//            return true;
//        }
//
//        return false;

        InputStream stream = client.getInputStream();
        AuthData authPacket = new AuthData(stream);

        PlayerData player = new PlayerData(authPacket, player_id_ctr++);
        players_data.put(client.getInetAddress(), player);
        player.startHandler(client, this);
        return true;
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
