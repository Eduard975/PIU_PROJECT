package entities;

import graphic.Sprite;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Vector3f;
import org.joml.Vector2f;

public abstract class EnemyBase {
    public float SIZE = Level.TILE_SIZE / 2.0f;

    public VertexArray mesh;
    public Texture texture;
    public Vector2f[] texCoords;
    public float[] tcs;
    public Sprite sprite;
    public float hp = 100;
    public int stunDuration = 0;
    protected float enemyScale;
    public long attackCooldownTimer = 999999;


    public EnemyBase(Vector3f pos) {
        position = pos;
    }

    public float angle;

    public Vector3f position;

    public int attackDamage;
    public int attackCooldown;
    public int xpWorth;


    abstract public void update();

    abstract public void render();

    public Vector3f getMinBounds() {
        return new Vector3f(position.x - SIZE / 2, position.y - SIZE / 2, 0);
    }

    public Vector3f getMaxBounds() {
        return new Vector3f(position.x + SIZE / 2, position.y + SIZE / 2, 0);
    }

    abstract public boolean canAttack();
}
