package server;

import com.mysql.cj.log.Log;
import configs.DimensionConfig;
import datas.AuthData;
import exceptions.BBCSQLAlreadyInLobby;
import exceptions.BBCSQLError;
import server.model.PlayerData;
import utils.Logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Dictionary;

public abstract class SQLDatabase {
    private static final String SQL_URL = "jdbc:mysql://localhost:3306/csit228f2";
    private static final String SQL_USER = "root";
    private static final String SQL_PASSWORD = "";

    private static boolean is_not_initialized = false;
    private static boolean is_instantiated = false;

    public static SQLPlayer getPlayerData(AuthData auth, int lobby_id) throws BBCSQLError {
        if (!is_instantiated) {
            is_instantiated = true;
            SQLIntialize();
        }

        SQLPlayer player = new SQLPlayer();
        StringBuilder sb = new StringBuilder();

        for (byte i : auth.id) {
            sb.append((char) i);
        }

        player.id = sb.toString();
        player.name = auth.name;

        if (is_not_initialized) {
            player.barrel_color = DimensionConfig.DEFAULT_COLOR_BARREL.clone();
            player.body_color = DimensionConfig.DEFAULT_COLOR_BODY.clone();
            player.border_color = DimensionConfig.DEFAULT_COLOR_BORDER.clone();
        } else {
            try (Connection conn = getConnection()) {
                getPlayer(conn, player);
                if (player.lobby_id != 0) throw new BBCSQLAlreadyInLobby(player.name);
                setLobby(player, lobby_id);
            } catch (SQLException ignored) {
                throw new BBCSQLError();
            }
        }

        return player;
    }

    public static void setLobby(SQLPlayer player, int lobby_id) throws BBCSQLError {
        if (is_not_initialized) {
            player.lobby_id = lobby_id;
            return;
        }

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE player SET lobby = ? WHERE id = ? AND name = ?");

            stmt.setInt(1, lobby_id);
            stmt.setString(2, player.id);
            stmt.setString(3, player.name);

            if (stmt.executeUpdate() > 0) {
                player.lobby_id = lobby_id;
            } else {
                throw new BBCSQLError("Unable to set lobby ID");
            }
        } catch (SQLException ignored) {
            throw new BBCSQLError();
        }
    }

    public static void closePlayer(SQLPlayer player, int score, int highest_score) throws BBCSQLError {
        if (highest_score < score) {
            highest_score = score;
        }

        if (is_not_initialized) {
            return;
        }

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE player SET lobby = 0, highest_score = ? WHERE id = ? AND name = ?");

            stmt.setInt(1, highest_score);
            stmt.setString(2, player.id);
            stmt.setString(3, player.name);

            if (stmt.executeUpdate() <= 0) {
                throw new BBCSQLError("Unable to close player");
            }
        } catch (SQLException ignored) {
            throw new BBCSQLError();
        }
    }

    public static void setName(SQLPlayer player, String new_name) throws BBCSQLError {
        if (is_not_initialized) {
            player.name = new_name;
            return;
        }

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE player SET name = ? WHERE id = ? AND name = ?");

            stmt.setString(1, new_name);
            stmt.setString(2, player.id);
            stmt.setString(3, player.name);

            if (stmt.executeUpdate() > 0) {
                player.name = new_name;
            } else {
                throw new BBCSQLError("Unable to set player name");
            }
        } catch (SQLException ignored) {
            throw new BBCSQLError();
        }
    }

    // set color in SQLPlayer
    public static void setColors(SQLPlayer player) throws BBCSQLError {
        if (is_not_initialized) {
            return;
        }


        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE player SET border = ?, body = ?, barrel = ? WHERE id = ? AND name = ?");

            stmt.setBytes(1, player.border_color);
            stmt.setBytes(2, player.body_color);
            stmt.setBytes(3, player.barrel_color);
            stmt.setString(4, player.id);
            stmt.setString(5, player.name);

            if (stmt.executeUpdate() <= 0) {
                throw new BBCSQLError("Unable to set colors");
            }
        } catch (SQLException ignored) {
            throw new BBCSQLError();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASSWORD);
    }

    private static void getPlayer(Connection conn, SQLPlayer player) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM player WHERE id = ? AND name = ?");

        stmt.setString(1, player.id);
        stmt.setString(2, player.name);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            player.lobby_id = res.getInt("lobby");
            player.highest_score = res.getInt("highest_score");
            player.body_color = res.getBytes("body");
            player.barrel_color = res.getBytes("barrel");
            player.border_color = res.getBytes("border");
        } else {
            stmt = conn.prepareStatement("INSERT INTO player (id, name, body, barrel, border) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, player.id);
            stmt.setString(2, player.name);

            StringBuilder sb = new StringBuilder();

            player.barrel_color = DimensionConfig.DEFAULT_COLOR_BARREL.clone();
            player.body_color = DimensionConfig.DEFAULT_COLOR_BODY.clone();
            player.border_color = DimensionConfig.DEFAULT_COLOR_BORDER.clone();

            stmt.setBytes(3, player.body_color);
            stmt.setBytes(4, player.barrel_color);
            stmt.setBytes(5, player.border_color);

            stmt.executeUpdate();
        }
    }

    private static void SQLIntialize() {
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate("UPDATE player SET lobby = 0");
            } catch (SQLException ignored) {
                stmt.execute("CREATE TABLE player (id VARCHAR(6) NOT NULL , name VARCHAR(255) NOT NULL , body BINARY(3) NOT NULL , barrel BINARY(3) NOT NULL , border BINARY(3) NOT NULL , lobby INT NOT NULL DEFAULT 0 , PRIMARY KEY (id(6), name(255)), highest_score INT NOT NULL DEFAULT 0)");
                stmt.executeUpdate("UPDATE player SET lobby = 0");
            }
        } catch (SQLException e) {
            is_not_initialized = true;
            Logging.error(SQLDatabase.class, "Unable to initialize database");
        }
    }

    public static class SQLPlayer {
        private String id;
        private int lobby_id = 0;

        public String name;
        public byte[] border_color;
        public byte[] body_color;
        public byte[] barrel_color;
        public int highest_score = 0;

        private SQLPlayer() {}
    }
}
