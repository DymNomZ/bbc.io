package server;

import configs.DimensionConfig;
import configs.SocketConfig;
import datas.*;
import server.debug.DebugWindow;
import server.game_structure.QuadRectangle;
import server.game_structure.QuadTree;
import server.game_structure.RangeCircle;
import server.model.*;
import utils.Logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

public class Lobby {
    public HashMap<UDPAddress, PlayerData> players_data = new HashMap<>();

    // TODO: May be replaced for quad trees data struct
    public WeakHashMap<PlayerData, ServerEntity> entity_data = new WeakHashMap<>();

    public LinkedList<PlayerData> spawn_queue = new LinkedList<>();
    static private int ID = 1;
    public final int id;
    private int player_id_ctr = 1;
    public QuadTree qtree;
    public final DatagramSocket input_socket;
    public boolean running = true;
    private final Thread game_thread, input_thread;

    private int awaited_player_id = 0;
    private InetAddress awaited_player_ip = null;
    private int awaited_player_port = 0;

    public Lobby() throws IOException {
        // TODO: Initialize Map (if required)
        id = ID++;
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
        qtree = new QuadTree(new QuadRectangle(0,0, DimensionConfig.MAP_WIDTH, DimensionConfig.MAP_HEIGHT),1, true);
        input_thread.start();
        game_thread.start();
    }

    private void gameThread() {
        while (running) {
            long game_clock = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            for (PlayerData i : players_data.values()) {
                sb.append(i.toString() + "\n");
            }
            DebugWindow.logPlayers(sb.toString());

            moveEntities(game_clock);

            handleSpawnQueue(game_clock);

            QuadTree current_tree =  constructQTree();

            quadCollisionCheck(current_tree);

            qtree = current_tree;

            long sleep_clock = Math.max(2 - (System.currentTimeMillis() - game_clock), 0);
            try {
                Thread.sleep(sleep_clock);
            } catch (InterruptedException e) {
                running = false;
                return;
            }
        }
    }

    private void quadCollisionCheck(QuadTree tree){
        for (ServerEntity entity: entity_data.values()) {
            RangeCircle circle = new RangeCircle(entity.x, entity.y, entity.radius + 1);
            List<ServerEntity> collision = tree.query(circle);
            for (ServerEntity other : collision) {
                if (entity != other) entity.isCollidingWith(other);
            }
        }
    }

    private QuadTree constructQTree(){
        QuadTree tree = new QuadTree(new QuadRectangle(0,0, DimensionConfig.MAP_WIDTH, DimensionConfig.MAP_HEIGHT),1, true);
        for(ServerEntity s : entity_data.values()){
            tree.insert(s);
        }

        return tree;
    }

    private void handleSpawnQueue(long game_clock){
        while (!spawn_queue.isEmpty()) {
            PlayerData player = spawn_queue.remove();
            entity_data.put(player, new PlayerEntity(game_clock,player.id));
        }
    }
    private void moveEntities(long game_clock){
        StringBuilder location_sb = new StringBuilder();
        for (PlayerData i : entity_data.keySet()) {
            ServerEntity entity = entity_data.get(i);

            if (entity instanceof ProjectileEntity) {
                entity.move(game_clock);
            } else {
                entity.move(game_clock, i.getInputs());
            }

            location_sb.append(entity.toString());
        }
        DebugWindow.log(location_sb.toString());
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

            PlayerData data = players_data.get(new UDPAddress(packet.getAddress(), packet.getPort()));

            if (packet.getLength() == 9 && data != null) {
                data.setInputs(new InputData(packet.getData()));
            } else if (packet.getLength() == 4) {
                int player_id = SerialData.decodeInt(packet.getData());
                if (player_id == awaited_player_id && packet.getAddress().equals(awaited_player_ip)) {
                    awaited_player_port = packet.getPort();
                }
            }
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
        OutputStream stdin = client.getOutputStream();
        AuthData authPacket = new AuthData(stream);

        awaited_player_ip = client.getInetAddress();
        awaited_player_port = 0;
        awaited_player_id = player_id_ctr;

        stdin.write(SerialData.convertInt(player_id_ctr));
        stdin.flush();

        long time_ms = System.currentTimeMillis();
        while (awaited_player_port == 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                awaited_player_id = 0;
                return true;
            }
            if (System.currentTimeMillis() - time_ms >= 3000) {
                awaited_player_id = 0;
                return true;
            }
        }

        stdin.write(255);
        stdin.flush();

        UDPAddress UDPAddr = new UDPAddress(awaited_player_ip, awaited_player_port);

        PlayerData player = new PlayerData(authPacket, player_id_ctr++);
        players_data.put(UDPAddr, player);
        player.startHandler(client, UDPAddr, this);
        return true;
    }

    public LobbyData initialLobbyData() {
        LinkedList<UserData> users = new LinkedList<>();

        for (PlayerData i : players_data.values()) {
            users.add(new UserData(i));
        }

        return new LobbyData(id, users);
    }
}
