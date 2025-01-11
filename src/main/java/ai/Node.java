package ai;

import math.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    private Vector3f position;
    private Node parent;
    private double gCost; // Cost from start to this node
    private double hCost; // Estimated cost from this node to end
    private double fCost; // Total cost (g + h)
    private List<Node> neighbors;
    private boolean walkable;

    public Node(Vector3f position) {
        this.position = position;
        this.neighbors = new ArrayList<>();
        this.walkable = true;
        this.gCost = 0;
        this.hCost = 0;
        this.fCost = 0;
        this.parent = null;
    }

    public void calculateCosts(Node startNode, Node endNode) {
        this.gCost = calculateDistance(startNode);
        this.hCost = calculateDistance(endNode);
        this.fCost = gCost + hCost;
    }

    public double calculateDistance(Node other) {
        float dx = position.x - other.position.x;
        float dy = position.y - other.position.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void addNeighbor(Node neighbor) {
        if (!neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
        }
    }

    public Vector3f getPosition() { return position; }
    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }
    public double getGCost() { return gCost; }
    public void setGCost(double gCost) { this.gCost = gCost; }
    public double getHCost() { return hCost; }
    public void setHCost(double hCost) { this.hCost = hCost; }
    public double getFCost() { return fCost; }
    public void setFCost(double fCost) { this.fCost = fCost; }
    public List<Node> getNeighbors() { return neighbors; }
    public boolean isWalkable() { return walkable; }
    public void setWalkable(boolean walkable) { this.walkable = walkable; }

    @Override
    public int compareTo(Node other) {
        if (this.fCost < other.fCost) return -1;
        if (this.fCost > other.fCost) return 1;
        return 0;
    }
}
