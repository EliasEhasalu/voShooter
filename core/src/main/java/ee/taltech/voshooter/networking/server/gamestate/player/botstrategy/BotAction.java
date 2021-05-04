package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;

public class BotAction {

    private boolean dashing;
    private boolean shooting;
    private MouseCoords aim;
    private int xMoveDir;
    private int yMoveDir;

    public boolean isDashing() {
        return dashing;
    }

    public boolean isShooting() {
        return shooting;
    }

    public MouseCoords getAim() {
        return aim;
    }

    public int getXMoveDir() {
        return xMoveDir;
    }

    public int getYMoveDir() {
        return yMoveDir;
    }
}
