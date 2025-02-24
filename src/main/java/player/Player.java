package player;

import entities.Ally;
import entities.EnemyBase;
import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private float SIZE = Level.TILE_SIZE / 2.0f;
    private VertexArray mesh;
    private Texture texture;

    public float speed = SIZE / 4;


    public ArrayList<Projectile> projectiles = new ArrayList<>();
    public ArrayList<Ally> allies = new ArrayList<>();
    public Explosion explosion;

    private float angle;
    public Vector3f position = new Vector3f();
    private Vector3f projectileDirection;
    private Vector3f mousePosition = new Vector3f();

    public int xp = 0;
    public int nextLevelXp = 2;
    public int currentLevel = 1;
    public float score;

    public float baseDamage = 1.0f;
    public float hp = 10;
    public float maxHp = 10;
    public int mp = 25;
    public int maxMp = 25;
    public long mpRegenCooldown = 2500;
    public long hpRegenCooldown = 4000;
    public long lastRegenTime = 0;
    public long lastHpRegenTime = 0;
    private List<EnemyBase> enemies;

    public Player() {
        float[] vertices = new float[]{
                -SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
                -SIZE / 2.0f, SIZE / 2.0f, 0.2f,
                SIZE / 2.0f, SIZE / 2.0f, 0.2f,
                SIZE / 2.0f, -SIZE / 2.0f, 0.2f
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("src/main/resources/images/player.png");
    }

    private boolean canRegenMp() {
        return (mp < maxMp) && (System.currentTimeMillis() - lastRegenTime) >= mpRegenCooldown;
    }

    private void regenMP() {
        if (!canRegenMp()) {
            return;
        }
        lastRegenTime = System.currentTimeMillis();
        mp += (int) (baseDamage * 3);
    }

    private boolean canRegenHp() {
        return (hp < maxHp) && (System.currentTimeMillis() - lastHpRegenTime) >= hpRegenCooldown;
    }

    private void regenHP() {
        if (!canRegenHp()) {
            return;
        }
        lastHpRegenTime = System.currentTimeMillis();
        hp += (int) baseDamage;
    }

    public void setEnemies(List<EnemyBase> enemies) {
        this.enemies = enemies;
    }

    public void update() {
        for (Projectile projectile : projectiles) {
            projectile.update();
        }

        ArrayList<Ally> alliesToRemove = new ArrayList<>();

        for (Ally ally : allies) {
            ally.update();
            if (enemies != null) {
                ally.findNewTarget(enemies);
            }
            if (ally.hp <= 0) {
                alliesToRemove.add(ally);
            }
        }

        for (Ally ally : alliesToRemove) {
            allies.remove(ally);
        }

        if (explosion != null) {
            explosion.update();
        }


        checkXp();
        regenMP();
        regenHP();
    }

    public void checkXp() {
        if (xp >= nextLevelXp) {
            int factor = (int) Math.pow(1.2, (currentLevel - 1));
            factor = Math.max(factor, 1);
            xp = xp - nextLevelXp;
            nextLevelXp = nextLevelXp + factor;
            levelUP();
        }
    }

    private void levelUP() {
        currentLevel++;
        int factor = (int) Math.pow(1.35, (currentLevel - 1));
        System.out.println(factor);
        factor = Math.max(factor, 1);
        maxMp = maxMp + factor;
        maxHp = (int)(maxHp + (factor * 0.7));
    }

    public void render() {
        Shader.PLAYER.enable();
        Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        texture.bind();
        mesh.render();
        for (Projectile projectile : projectiles) {
            projectile.render();
        }
        for (Ally ally : allies) {
            ally.render();
        }
        if (explosion != null) {
            explosion.render();
        }
        Shader.PLAYER.disable();
    }


    public Vector3f getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Vector3f getMinBounds() {
        return new Vector3f(position.x - SIZE / 2, position.y - SIZE / 2, 0);
    }

    public Vector3f getMaxBounds() {
        return new Vector3f(position.x + SIZE / 2, position.y + SIZE / 2, 0);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setProjectileDirection() {
        this.projectileDirection = new Vector3f(mousePosition.x - position.x, mousePosition.y - position.y, 0.0f);
    }

    public Vector3f getProjectileDirection() {
        return projectileDirection;
    }

    public void setMousePosition(Vector3f mousePosition) {
        this.mousePosition = mousePosition;
        setProjectileDirection();
    }

    public Vector3f getMousePosition() {
        return mousePosition;
    }

    public float getBaseDamage(){
        return baseDamage;
    }
}
