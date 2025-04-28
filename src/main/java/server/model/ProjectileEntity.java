package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;
import datas.InputData;

public class ProjectileEntity extends ServerEntity{

    public ProjectileEntity(long game_clock, int owner_id, double angle) {
        super(game_clock, DimensionConfig.PROJECTILE_RADIUS, owner_id,angle);
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

        double velocity = StatsConfig.PROJECTILE_SPEED * offset;
        double vx = velocity * Math.cos(angle);
        double vy = velocity * Math.sin(angle);
        double deltax = vx;
        double deltay = vy;






        x += deltax;
        y += deltay;

    }

    @Override
    public EntityData getEntityData() {
        return new EntityData(player_id, x, y, angle);
    }
}
