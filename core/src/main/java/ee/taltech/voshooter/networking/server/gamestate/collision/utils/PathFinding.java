package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

public class PathFinding {

    private static final Set<IntPair> EIGHT_DIRS = new HashSet<IntPair>() {{
       add(new IntPair(0, 1)); add(new IntPair(0, -1));
       add(new IntPair(1, 0)); add(new IntPair(-1, 0));
       add(new IntPair(1, 1)); add(new IntPair(-1, -1));
       add(new IntPair(1, -1)); add(new IntPair(-1, 1));
    }};

    public static List<Node> bfsPath(Node start, Node target, int[][] grid) {
        boolean[][] discovered = new boolean[grid.length][grid[0].length];
        discovered[start.getY()][start.getX()] = true;

        Deque<Node> q = new LinkedBlockingDeque<>();
        q.addLast(start);

        boolean found = false;
        while (!q.isEmpty()) {
            Node v = q.removeFirst();
            if (v.getX() == target.getX() && v.getY() == target.getY()) {
                found = true;
                target = v;
                break;
            }
            for (IntPair dir : EIGHT_DIRS) {
                int x = v.getX() + dir.x;
                int y = v.getY() + dir.y;
                if (withinGrid(x, y, grid.length, grid[0].length) && grid[y][x] != 1 && !discovered[y][x]) {
                    discovered[y][x] = true;
                    Node neighbour = new Node(x, y, grid[y][x]);
                    neighbour.setParent(v);
                    q.addLast(neighbour);
                }
            }
        }

        return getPathToTarget(start, target);
    }

    private static List<Node> getPathToTarget(Node start, Node target) {
        List<Node> path = new LinkedList<>();
        path.add(target);

        Node current = target;
        while (current.getParent() != null) {
            path.add(current.getParent());
            current = current.getParent();
        }

        Collections.reverse(path);
        return path;
    }

    private static boolean withinGrid(int x, int y, int gridWidth, int gridHeight) {
        return (
               x >= 0 && x < gridWidth
            && y >= 0 && y < gridHeight
        );
    }
}
