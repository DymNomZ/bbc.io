package server.model;

import configs.DimensionConfig;
import datas.EntityData;

public class ProjectileEntity extends ServerEntity{

    public ProjectileEntity(long game_clock, int owner_id) {
        super(game_clock, DimensionConfig.PROJECTILE_RADIUS, owner_id);
    }

    @Override
    public boolean isCollidingWith(ServerEntity other) {
        return false;
    }

    @Override
    public void handleCollision(ServerEntity other) {

    }

    @Override
    public EntityData getEntityData() {
        return new EntityData(player_id, x, y, angle);
    }
}
