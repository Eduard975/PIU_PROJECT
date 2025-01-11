package player;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private float SIZE = Level.TILE_SIZE / 2.0f;
    private VertexArray mesh;
    private Texture texture;

    private float speed = SIZE / 2;
    
    public ArrayList<Projectile> projectiles = new ArrayList<>();

    private float angle;
    private Vector3f position = new Vector3f();
    private Vector3f projectileDirection;

    private AbilityBase[] abilities;

    long windowId = GLFW.glfwGetCurrentContext();

    public float hp = 100;
    public float maxHp = 100;

    public int mp = 25;
    public int maxMp = 25;

    public long mpRegenCooldown = 4000;
    public long lastRegenTime = 0;

    public void initAbilities() {
        abilities = new AbilityBase[]{
                new ProjectileAbility(5, 2000, this),
        };
    }

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
        texture = new Texture("src/main/resources/player.png");

        initAbilities();
    }

    private boolean canRegenMp() {
        return (mp < maxMp) && (System.currentTimeMillis() - lastRegenTime) >= mpRegenCooldown;
    }

    private void regenMP() {
        if (!canRegenMp()) {
            return;
        }
        System.out.println(mp);
        lastRegenTime = System.currentTimeMillis();
        mp += 5;
    }

    public void update() {
        if (glfwGetKey(windowId, GLFW_KEY_S) == GLFW_PRESS) {
            position.y -= speed;
            if (position.y < -Level.yBounds) {
                position.y = -Level.yBounds;
            }
        }
        if (glfwGetKey(windowId, GLFW_KEY_W) == GLFW_PRESS) {
            position.y += speed;
            if (position.y > Level.yBounds) {
                position.y = Level.yBounds;
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_A) == GLFW_PRESS) {
            position.x -= speed;
            if (position.x < -Level.xBounds) {
                position.x = -Level.xBounds;
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_D) == GLFW_PRESS) {
            position.x += speed;
            if (position.x > Level.xBounds) {
                position.x = Level.xBounds;
            }
        }

        if (glfwGetKey(windowId, GLFW_KEY_E) == GLFW_PRESS) {
            if (abilities[0].canUse(mp)) {
                abilities[0].use(mp);
                mp -= abilities[0].getCost();
            }
        }

        for (Projectile projectile : projectiles) {
            projectile.update();
        }

        regenMP();
    }

    public void render() {
        Shader.PLAYER.enable();
        Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        texture.bind();
        mesh.render();
        for (Projectile projectile : projectiles) {
            projectile.render();
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

    public void setProjectileDirection(Vector3f projectileDirection) {
        this.projectileDirection = new Vector3f(projectileDirection.x - position.x, projectileDirection.y - position.y, 0.0f);
    }

    public Vector3f getProjectileDirection() {
        return projectileDirection;
    }
}
