package ai;

import math.Vector3f;
import java.util.*;

public class PathFinder {
    private List<Node> nodes;
    private float gridSize;
    private int gridWidth;
    private int gridHeight;
    private final float worldWidth;
    private final float worldHeight;

    public PathFinder(float width, float height, float gridSize) {
        this.worldWidth = width;
        this.worldHeight = height;
        this.gridSize = gridSize;
        this.gridWidth = (int)(width / gridSize);
        this.gridHeight = (int)(height / gridSize);
        this.nodes = new ArrayList<>();

        // Initialize nodes
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Vector3f pos = new Vector3f(
                        x * gridSize + gridSize/2, // Center the node in the grid cell
                        y * gridSize + gridSize/2,
                        0.2f
                );
                nodes.add(new Node(pos));
            }
        }

        // Connect nodes
        connectNodes();
    }

    private void connectNodes() {
        for (Node node : nodes) {
            Vector3f pos = node.getPosition();
            int x = (int)(pos.x / gridSize);
            int y = (int)(pos.y / gridSize);

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;

                    int newX = x + dx;
                    int newY = y + dy;

                    if (isValidGridPosition(newX, newY)) {
                        Node neighbor = getNodeAt(newX, newY);
                        if (neighbor != null) {
                            node.addNeighbor(neighbor);
                        }
                    }
                }
            }
        }
    }

    public List<Vector3f> findPath(Vector3f start, Vector3f end) {
        // Clamp the start and end positions to world bounds
        Vector3f clampedStart = clampToWorld(start);
        Vector3f clampedEnd = clampToWorld(end);

        Node startNode = findNearestNode(clampedStart);
        Node endNode = findNearestNode(clampedEnd);

        if (startNode == null || endNode == null) {
            return new ArrayList<>();
        }

        // Reset all nodes for new path calculation
        for (Node node : nodes) {
            node.setParent(null);
            node.setGCost(Double.MAX_VALUE);
            node.setHCost(0);
            node.setFCost(Double.MAX_VALUE);
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();

        startNode.setGCost(0);
        startNode.setHCost(startNode.calculateDistance(endNode));
        startNode.setFCost(startNode.getHCost());
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current == endNode) {
                return smoothPath(reconstructPath(endNode));
            }

            closedSet.add(current);

            for (Node neighbor : current.getNeighbors()) {
                if (!neighbor.isWalkable() || closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGCost = current.getGCost() + current.calculateDistance(neighbor);

                if (!openSet.contains(neighbor) || tentativeGCost < neighbor.getGCost()) {
                    neighbor.setParent(current);
                    neighbor.setGCost(tentativeGCost);
                    neighbor.setHCost(neighbor.calculateDistance(endNode));
                    neighbor.setFCost(neighbor.getGCost() + neighbor.getHCost());

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private Vector3f clampToWorld(Vector3f position) {
        return new Vector3f(
                Math.max(0, Math.min(position.x, worldWidth)),
                Math.max(0, Math.min(position.y, worldHeight)),
                position.z
        );
    }

    private boolean isValidGridPosition(int x, int y) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }

    private List<Vector3f> smoothPath(List<Vector3f> path) {
        if (path.size() <= 2) return path;

        List<Vector3f> smoothedPath = new ArrayList<>();
        smoothedPath.add(path.get(0));

        for (int i = 1; i < path.size() - 1; i++) {
            Vector3f prev = path.get(i - 1);
            Vector3f current = path.get(i);
            Vector3f next = path.get(i + 1);

            // Skip points that are roughly collinear
            Vector3f v1 = new Vector3f(current.x - prev.x, current.y - prev.y, 0);
            Vector3f v2 = new Vector3f(next.x - current.x, next.y - current.y, 0);
            float angle = Math.abs(angleBetweenVectors(v1, v2));

            if (angle > 0.2f) { // Threshold for considering a point significant
                smoothedPath.add(current);
            }
        }

        smoothedPath.add(path.get(path.size() - 1));
        return smoothedPath;
    }

    private float angleBetweenVectors(Vector3f v1, Vector3f v2) {
        float dot = v1.x * v2.x + v1.y * v2.y;
        float v1Len = (float)Math.sqrt(v1.x * v1.x + v1.y * v1.y);
        float v2Len = (float)Math.sqrt(v2.x * v2.x + v2.y * v2.y);

        if (v1Len == 0 || v2Len == 0) return 0;
        return (float)Math.acos(dot / (v1Len * v2Len));
    }

    private Node findNearestNode(Vector3f position) {
        int x = (int)(position.x / gridSize);
        int y = (int)(position.y / gridSize);
        return getNodeAt(x, y);
    }

    private Node getNodeAt(int x, int y) {
        if (x < 0 || x >= gridWidth || y < 0 || y >= gridHeight) {
            return null;
        }
        return nodes.get(y * gridWidth + x);
    }

    private List<Vector3f> reconstructPath(Node endNode) {
        List<Vector3f> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(0, current.getPosition());
            current = current.getParent();
        }

        if (path.isEmpty()) {
            System.out.println("Error: Path contains null or is empty!");
        }

        return path;
    }

}