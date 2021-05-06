package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

public class Node {

    private final int x;
    private final int y;
    private final int value;
    private Node parent = null;

    public Node(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Node(int[] pos, int value) {
        this.x = pos[0];
        this.y = pos[1];
        this.value = value;
    }

    public void setParent(Node parentNode) {
        this.parent = parentNode;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node getParent() {
        return parent;
    }

    public int[] subtract(Node other) {
        return new int[] {this.x - other.x, this.y - other.y};
    }

    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
