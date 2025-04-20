package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import datas.SerialData;

import java.util.Random;

public class ServerEntity {
    double x, y, angle;
    Region region;
    byte[] id;
    public final boolean is_projectile;
    double speed;
    private long last_moved_time;

    public ServerEntity(byte[] player_id, double angle, long game_clock) {
        // Randomize x & y (for Player spawning)

        id = player_id;
        this.angle = angle;

        Random rand = new Random();
        x = rand.nextDouble() % DimensionConfig.MAP_WIDTH;
        y = rand.nextDouble() % DimensionConfig.MAP_HEIGHT;

        is_projectile = false;
        speed = StatsConfig.PLAYER_SPEED;
        last_moved_time = game_clock;
    }

    public ServerEntity(byte[] owner_id, double x, double y, double angle, long game_clock) {
        id = owner_id;
        this.angle = angle;
        this.x = x;
        this.y = y;

        is_projectile = true;
        speed = StatsConfig.PROJECTILE_SPEED;
        last_moved_time = game_clock;
    }

    public void move(long game_clock) {
        long offset = game_clock - last_moved_time;
        last_moved_time = game_clock;

        // TODO: x and y conversion from angle
        x += speed * offset;
    }

    // TODO: May be change for collisions
    public void move(long game_clock, InputData inputs) {
        long offset = game_clock - last_moved_time;
        last_moved_time = game_clock;

        angle = inputs.angle;

        double delta = speed * offset;

        if (inputs.up_pressed) {
            y -= delta;
        }
        if (inputs.down_pressed) {
            y += delta;
        }
        if (inputs.left_pressed) {
            x -= delta;
        }
        if (inputs.right_pressed) {
            x += delta;
        }
    }

    public EntityData getEntityData() {
        return new EntityData(id, x, y, angle, is_projectile);
    }
}
