package ai;

import math.Vector3f;
import java.util.*;

public class PathFinder {
    private List<Node> nodes;
    private float gridSize;
    private int gridWidth;
    private int gridHeight;

    public PathFinder(float width, float height, float gridSize) {
        this.gridSize = gridSize;
        this.gridWidth = (int)(width / gridSize);
        this.gridHeight = (int)(height / gridSize);
        this.nodes = new ArrayList<>();

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Vector3f pos = new Vector3f(x * gridSize, y * gridSize, 0.2f);
                nodes.add(new Node(pos));
            }
        }

        // Connect neighboring nodes
        for (Node node : nodes) {
            Vector3f pos = node.getPosition();
            int x = (int)(pos.x / gridSize);
            int y = (int)(pos.y / gridSize);

            // Add neighbors (including diagonals)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;

                    int newX = x + dx;
                    int newY = y + dy;

                    if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
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
        Node startNode = findNearestNode(start);
        Node endNode = findNearestNode(end);

        if (startNode == null || endNode == null) {
            return new ArrayList<>();
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();

        openSet.add(startNode);
        startNode.setGCost(0);
        startNode.setHCost(startNode.calculateDistance(endNode));
        startNode.setFCost(startNode.getHCost());

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current == endNode) {
                return reconstructPath(endNode);
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

        return new ArrayList<>(); // No path found
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
        return nodes.get(x * gridHeight + y);
    }

    private List<Vector3f> reconstructPath(Node endNode) {
        List<Vector3f> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(0, current.getPosition());
            current = current.getParent();
        }

        return path;
    }

    public void setObstacle(Vector3f position, boolean isObstacle) {
        Node node = findNearestNode(position);
        if (node != null) {
            node.setWalkable(!isObstacle);
        }
    }
}