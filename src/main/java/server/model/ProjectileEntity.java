package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import utils.Logging;

public class ProjectileEntity extends ServerEntity{
    private long spawn_time;

    public ProjectileEntity(long game_clock, int owner_id, double angle) {
        super(game_clock, DimensionConfig.PROJECTILE_RADIUS, owner_id,angle);

        this.angle = -angle;
        spawn_time = game_clock;

        if (this.angle < 0) {
            this.angle = 360 - angle;
        }
    }

    public boolean isAlive(long game_clock) {
        return game_clock - spawn_time < StatsConfig.PROJECTILE_LIFETIME;
    }

    @Override
    public boolean isCollidingWith(ServerEntity other) {
        return false;
    }

    @Override
    public void handleCollision(ServerEntity other) {

    }

    @Override
    public void move(long game_clock, PlayerData playerData) {
        long offset = game_clock - last_moved_time;
        last_moved_time = game_clock;

        //Logging.write(this,"Moving towards" + angle);

        double velocity = StatsConfig.PROJECTILE_SPEED * offset;
        double delta_x = velocity * Math.cos(Math.toRadians(angle));
        double delta_y = velocity * Math.sin(Math.toRadians(angle));

        x += delta_x;
        y -= delta_y;

        // TODO: bounds collision
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
        return new EntityData(player_id, x, y, angle);
    }
}
