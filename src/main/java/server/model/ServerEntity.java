package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import datas.SerialData;

import java.util.Random;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import datas.SerialData;
import interfaces.Collidable;

import java.util.Random;

public abstract class ServerEntity implements Collidable<ServerEntity> {
    public double x, y, angle, radius;
    public final int player_id;
    double speed;
    private long last_moved_time;

    public ServerEntity(long game_clock, double radius, int player_id) {
        this.radius = radius;
        this.player_id = player_id;

        this.angle = 0;
        Random rand = new Random();
        x = rand.nextDouble() % DimensionConfig.MAP_WIDTH;
        y = rand.nextDouble() % DimensionConfig.MAP_HEIGHT;
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

    public abstract EntityData getEntityData();
}
