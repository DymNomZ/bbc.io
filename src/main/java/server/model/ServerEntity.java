package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import java.util.Random;
import interfaces.Collidable;

public abstract class ServerEntity implements Collidable<ServerEntity> {
    public double x, y, angle, radius;
    public final int player_id;
    double speed;
    private long last_moved_time;

    private final double x_map_offset, y_map_offset;

    public ServerEntity(long game_clock, double radius, int player_id) {
        this.radius = radius;
        this.player_id = player_id;
        x_map_offset = DimensionConfig.MAP_WIDTH - radius * 2;
        y_map_offset = DimensionConfig.MAP_HEIGHT - radius * 2;

        this.angle = 0;
        Random rand = new Random();
        x = rand.nextDouble(0, 10);
        y = rand.nextDouble(0, 10);
        speed = StatsConfig.PLAYER_SPEED;
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
        if (inputs.lShift_pressed) {
            delta *= 1.5;
        }

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


        if (x < 0) {
            x = 0;
        } else if (x > x_map_offset) {
            x = x_map_offset;
        }

        if (y < 0) {
            y = 0;
        } else if (y > y_map_offset) {
            y = y_map_offset;
        }
    }

    @Override
    public String toString() {
        return String.format("[Player]\nID: %d\nx: %f, y: %f", player_id, x, y);
    }

    public abstract EntityData getEntityData();
}
