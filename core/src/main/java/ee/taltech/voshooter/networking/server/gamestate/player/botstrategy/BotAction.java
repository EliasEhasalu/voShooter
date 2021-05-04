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

    void setDashing(boolean dashing) {
        this.dashing = dashing;
    }

    void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    void setAim(MouseCoords aim) {
        this.aim = aim;
    }

    void setXMoveDir(int xMoveDir) {
        this.xMoveDir = xMoveDir;
    }

    void setYMoveDir(int yMoveDir) {
        this.yMoveDir = yMoveDir;
    }
}
