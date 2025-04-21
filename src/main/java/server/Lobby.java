package server;

import configs.DimensionConfig;
import datas.*;
import server.model.PlayerData;
import server.model.PlayerEntity;
import server.model.ProjectileEntity;
import server.model.ServerEntity;
import quadtree.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;


public class Lobby {
    public HashMap<InetAddress, PlayerData> players_data = new HashMap<>();

    QuadTree qtree;
    public WeakHashMap<PlayerData, ServerEntity> entity_data = new WeakHashMap<>();

    public LinkedList<PlayerData> spawn_queue = new LinkedList<>();
    static private int ID = 1;
    static private final int MAX_PLAYERS = 20;
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

        qtree = new QuadTree(new QuadRectangle(0,0, DimensionConfig.MAP_WIDTH,DimensionConfig.MAP_HEIGHT),1);
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
        if (players_data.size() < MAX_PLAYERS) {
            InputStream stream = client.getInputStream();
            AuthData authPacket = new AuthData(stream);

            PlayerData player = new PlayerData(authPacket, client, this);
            players_data.put(client.getInetAddress(), player);
            return true;
        }

        return false;
    }

    public LobbyData initialLobbyData() {
        LobbyData data = new LobbyData();
        data.id = id;
        for (PlayerData i : players_data.values()) {
            data.users.add(new UserData(i.id, i.name, i.score));
        }

        return data;
    }
}
