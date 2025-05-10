package server;

import configs.DimensionConfig;
import configs.SocketConfig;
import configs.StatsConfig;
import datas.*;
import exceptions.BBCSQLError;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {
    public ConcurrentHashMap<UDPAddress, PlayerData> players_data = new ConcurrentHashMap<>();

    // TODO: May be replaced for quad trees data struct
    public ConcurrentHashMap<PlayerData, ArrayList<ServerEntity>> entity_data = new ConcurrentHashMap<>();

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

        input_thread = new Thread(this::inputThread);
        game_thread = new Thread(this::gameThread);
        qtree = new QuadTree(new QuadRectangle(0,0, DimensionConfig.MAP_WIDTH, DimensionConfig.MAP_HEIGHT),1, true, 0);
        input_thread.start();
        game_thread.start();
    }

    private void gameThread() {
        while (running) {
            long game_clock = System.currentTimeMillis();

            // TODO: Logging in every loop will greatly hinder the game thread (Remove at production)
            if (ServerMain.DEBUG_WINDOW) {
                StringBuilder sb = new StringBuilder();
                for (PlayerData i : players_data.values()) {
                    sb.append(i.toString()).append("\n");
                }
                DebugWindow.logPlayers(sb.toString());
            }

            spawnProjectiles(game_clock);

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

    private void spawnProjectiles(long game_clock) {
            for (PlayerData i : players_data.values()) {
                InputData inputData = i.getInputs();
                if (inputData.lClick_pressed) {
                    shoot(i, game_clock);
                }
            }
    }

    private void shoot(PlayerData playerData, long game_clock){
            if (game_clock - playerData.last_shoot >= StatsConfig.PLAYER_SHOOT_COOLDOWN) {
                ArrayList<ServerEntity> chain = entity_data.get(playerData);

                if (chain != null) {
                    ProjectileEntity p = new ProjectileEntity(game_clock, playerData.id, playerData.getInputs().angle, playerData.player_entity.damage);
                    p.x = chain.get(0).x;
                    p.y = chain.get(0).y;
                    entity_data.get(playerData).add(p);
                    playerData.last_shoot = game_clock;
                }
            }
    }

    private void quadCollisionCheck(QuadTree tree){
            for (ArrayList<ServerEntity> i : entity_data.values()) {
                for (ServerEntity entity : i) {
                    RangeCircle circle = new RangeCircle(entity.x, entity.y, entity.radius + 1);
                    List<ServerEntity> collision = tree.query(circle);
                    for (ServerEntity other : collision) {
                        if (entity != other) {
                            handleCollision(entity, other);
                        }
                    }
                }
            }
    }

    private void handleCollision(ServerEntity entity, ServerEntity other_entity){
        if(entity.isCollidingWith(other_entity)){
            entity.handleCollision(other_entity);
        }
    }

    private QuadTree constructQTree(){
        QuadTree tree = new QuadTree(new QuadRectangle(0,0, DimensionConfig.MAP_WIDTH, DimensionConfig.MAP_HEIGHT),2, true,0);

        for(Iterator<PlayerData> keys = entity_data.keys().asIterator(); keys.hasNext();) {
            PlayerData key = keys.next();
            ArrayList<ServerEntity> i = entity_data.get(key);

            PlayerEntity entity = null;
            for(Iterator<ServerEntity> iterator = i.iterator(); iterator.hasNext();) {
                ServerEntity s = iterator.next();
                if(s instanceof ProjectileEntity sProjectile && sProjectile.has_collided){
                    entity.stat_upgradable += key.addScore(sProjectile.score);

                    iterator.remove();
                    continue;
                } else if(s instanceof PlayerEntity && ((PlayerEntity)s).health <= 0){
                    handleDeath((PlayerEntity)s);
                    key.resetScore();
                    break;
                } else if (s instanceof PlayerEntity) {
                    entity = (PlayerEntity) s;
                }
                tree.insert(s);
            }

        }

        return tree;
    }

    private void handleDeath(PlayerEntity s) {
        PlayerData killer_data = getPlayerDataWithId(s.last_hit_player_id);
        PlayerData victim_data = getPlayerDataWithId(s.player_id);

        assert killer_data != null;
        assert victim_data != null;

        String death_message = DeathMessageGenerator.getRandomDeathMessage(victim_data.name,killer_data.name);

        entity_data.remove(victim_data);


    }
    private PlayerData getPlayerDataWithId(int id){
        for(PlayerData p : players_data.values()) {
            if(p.id == id){
                return p;
            }
        }
        return null;
    }

    private void handleSpawnQueue(long game_clock){
        while (!spawn_queue.isEmpty()) {
            PlayerData player = spawn_queue.remove();
            ArrayList<ServerEntity> entities = new ArrayList<>();
            player.player_entity = new PlayerEntity(game_clock,player.id);
            entities.add(player.player_entity);
            entity_data.put(player, entities);
        }
    }
    private void moveEntities(long game_clock){
        StringBuilder location_sb = new StringBuilder();
        for (PlayerData i : entity_data.keySet()) {

            // TODO: possible that .get may return null
            for (Iterator<ServerEntity> iter = entity_data.get(i).iterator(); iter.hasNext();) {
                ServerEntity e = iter.next();

                if (e instanceof ProjectileEntity p && !p.isAlive(game_clock)) {
                    iter.remove();
                    continue;
                }

                e.move(game_clock, i);

                // TODO: Logging in every loop will greatly hinder the game thread (Remove at production)
                location_sb.append(e.toString());
            }
        }

        if (ServerMain.DEBUG_WINDOW) {
            DebugWindow.log(location_sb.toString());
        } else {
            Logging.write(this, location_sb.toString());
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

        try {
            PlayerData player = new PlayerData(authPacket, player_id_ctr++, id);
            players_data.put(UDPAddr, player);
            player.startHandler(client, UDPAddr, this);
        } catch (BBCSQLError e) {
            player_id_ctr--;
            Logging.error(this, e.getMessage());
            throw new IOException();
        }

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
