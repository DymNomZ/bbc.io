package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import java.util.Random;
import interfaces.Collidable;
import javafx.scene.shape.Circle;

public abstract class ServerEntity implements Collidable<ServerEntity> {
    public double x, y, angle, radius;
    public final int player_id;
    double speed;
    protected long last_moved_time;

    protected final double x_map_offset, y_map_offset;

    public ServerEntity(long game_clock, double radius, int player_id) {
        this.radius = radius;
        this.player_id = player_id;
        x_map_offset = DimensionConfig.MAP_WIDTH - radius * 2;
        y_map_offset = DimensionConfig.MAP_HEIGHT - radius * 2;

        this.angle = 0;
        Random rand = new Random();
        x = rand.nextDouble(0, DimensionConfig.MAP_WIDTH - radius * 2);
        y = rand.nextDouble(0, DimensionConfig.MAP_HEIGHT - radius * 2);
        speed = StatsConfig.PLAYER_SPEED;
        last_moved_time = game_clock;
    }

    public abstract Circle getHitbox();


    public ServerEntity(long game_clock, double radius, int player_id, double angle) {
        this.radius = radius;
        this.player_id = player_id;
        x_map_offset = DimensionConfig.MAP_WIDTH - radius * 2;
        y_map_offset = DimensionConfig.MAP_HEIGHT - radius * 2;

        this.angle = angle;
        speed = StatsConfig.PROJECTILE_SPEED;
        last_moved_time = game_clock;

    }

    // TODO: May be change for collisions
    public abstract void move(long game_clock, PlayerData playerData);

    @Override
    public String toString() {
        return String.format("[Player]\nID: %d\nx: %f, y: %f", player_id, x, y);
    }

    public abstract EntityData getEntityData();
}
