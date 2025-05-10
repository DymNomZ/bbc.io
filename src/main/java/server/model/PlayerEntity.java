package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import javafx.scene.shape.Circle;
import utils.Logging;

public class PlayerEntity extends ServerEntity{
    public int stat_upgradable;
    public int maximum_health;
    public int health;
    public int damage;
    public int last_hit_player_id;

    // Last player got damaged time in millisecond (Used to render damage animations in client)
    public long time_damaged;

    public PlayerEntity(long game_clock, int player_id) {
        super(game_clock, DimensionConfig.PLAYER_RADIUS, player_id);
        health = StatsConfig.PLAYER_HEALTH;
        maximum_health = health;
        damage = (int) StatsConfig.PROJECTILE_DAMAGE;
        time_damaged = 0;
        stat_upgradable = 0;
    }

    // if Killed, return true
    public boolean damage(int amount){
        if (health <= 0) return false;

        health -= amount;

        return health <= 0;
    }

    @Override
    public boolean isCollidingWith(ServerEntity other) {
        double buffer = 5.0; //Adjust this value for leeway
        Circle c1 = getHitbox();
        Circle c2 = other.getHitbox();

        double dx = c1.getCenterX() - c2.getCenterX();
        double dy = c1.getCenterY() - c2.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= (c1.getRadius() + c2.getRadius() + buffer);
    }

    @Override
    public void handleCollision(ServerEntity other, long game_clock) {
        if (other instanceof PlayerEntity playerEntity) {
            if (time_damaged + 200 < game_clock) {
                time_damaged = game_clock;
                health -= 5;
            }

            if (playerEntity.time_damaged + 200 < game_clock) {
                playerEntity.time_damaged = game_clock;
                playerEntity.health -= 5;
            }
        }
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


        if (x < 0 + DimensionConfig.PLAYER_RADIUS) {
            x = 0 + DimensionConfig.PLAYER_RADIUS;
        } else if (x > x_map_offset) {
            x = x_map_offset;
        }

        if (y < 0 + DimensionConfig.PLAYER_RADIUS) {
            y = 0 + 0 + DimensionConfig.PLAYER_RADIUS;
        } else if (y > y_map_offset) {
            y = y_map_offset;
        }
    }

    @Override
    public EntityData getEntityData() {
        return new EntityData(player_id, x, y, angle, stat_upgradable, maximum_health, health, speed, damage, time_damaged);
    }
}
