package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import javafx.scene.shape.Circle;
import utils.Logging;

public class PlayerEntity extends ServerEntity{
    public int health;

    // Last player got damaged time in millisecond (Used to render damage animations in client)
    public long time_damaged;

    public PlayerEntity(long game_clock, int player_id) {
        super(game_clock, DimensionConfig.PLAYER_RADIUS, player_id);
        health = StatsConfig.PLAYER_HEALTH;
        time_damaged = 0;
    }

    public void damage(double amount){
        Logging.write(this,"I GOT DAMAGED");
        health -= (int) amount;
    }

    @Override
    public boolean isCollidingWith(ServerEntity other) {

        return false;
    }

    @Override
    public void handleCollision(ServerEntity other) {

    }

    @Override
    public Circle getHitbox() {
        return new Circle(DimensionConfig.PLAYER_RADIUS);
    }

    @Override
    public void move(long game_clock, PlayerData playerData) {
        InputData inputs = playerData.getInputs();

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
    public EntityData getEntityData() {
        return new EntityData(player_id, x, y, angle, health, time_damaged);
    }
}
