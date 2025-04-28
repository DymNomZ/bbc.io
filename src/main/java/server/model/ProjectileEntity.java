package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;
import utils.Logging;

public class ProjectileEntity extends ServerEntity{

    public ProjectileEntity(long game_clock, int owner_id, double angle) {
        super(game_clock, DimensionConfig.PROJECTILE_RADIUS, owner_id,angle);

        this.angle = -angle;

        if (angle >= 0) {
            this.angle = 360 - angle;
        }

        Logging.write(this, this.angle + " angle");
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
        double delta_x = velocity * Math.cos(angle);
        double delta_y = velocity * Math.sin(angle);

        x += delta_x;
        y += delta_y;

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
