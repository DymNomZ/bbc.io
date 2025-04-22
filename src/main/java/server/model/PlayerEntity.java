package server.model;

import configs.DimensionConfig;
import configs.StatsConfig;
import datas.EntityData;

public class PlayerEntity extends ServerEntity{
    public int health;

    // Last player got damaged time in millisecond (Used to render damage animations in client)
    public long time_damaged;

    public PlayerEntity(long game_clock, int player_id) {
        super(game_clock, DimensionConfig.PLAYER_RADIUS, player_id);
        health = StatsConfig.PLAYER_HEALTH;
        time_damaged = 0;
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
        return new EntityData(player_id, x, y, angle, health, time_damaged);
    }
}
