package server.model;

import configs.DimensionConfig;

public class PlayerEntity extends ServerEntity{

    public PlayerEntity(long game_clock, byte[] player_id) {
        super(game_clock, DimensionConfig.PLAYER_RADIUS, player_id);
    }

    @Override
    public boolean isCollidingWith(ServerEntity other) {

        return false;
    }

    @Override
    public void handleCollision(ServerEntity other) {

    }
}
