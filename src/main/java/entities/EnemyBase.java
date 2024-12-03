package entities;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public abstract class EnemyBase {
    public float SIZE = Level.TILE_SIZE/2.0f;
    public VertexArray mesh;
    public Texture texture;
    public float hp = 100;


    public int angle;

    public Vector3f position;

    public int attackDamage;
    public int attackCooldown;


    abstract public void update();

    abstract public void render();

    public Vector3f getMinBounds() {
        return new Vector3f(position.x - SIZE / 2, position.y - SIZE / 2, 0);
    }

    public Vector3f getMaxBounds() {
        return new Vector3f(position.x + SIZE / 2, position.y + SIZE / 2, 0);
    }
}
