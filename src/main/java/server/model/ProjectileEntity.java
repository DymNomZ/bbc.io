package server.model;

import configs.DimensionConfig;

public class ProjectileEntity extends ServerEntity{

    public ProjectileEntity(long game_clock, byte[] owner_id) {
        super(game_clock, DimensionConfig.PROJECTILE_RADIUS, owner_id);
    }

    @Override
    public boolean isCollidingWith(ServerEntity other) {
        return false;
    }

    @Override
    public void handleCollision(ServerEntity other) {

    }
}
