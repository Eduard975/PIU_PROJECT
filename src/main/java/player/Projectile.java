package player;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;

public class Projectile {
    private float SIZE = Level.TILE_SIZE / 8.0f;
    private VertexArray mesh;
    private Texture texture;

    private float angle;
    private Vector3f position;
    private Vector3f direction;
    private float speed = 10;

    public float damage;
    public float scalingFactor = 0.5f;

    public Projectile(Vector3f playerPosition, float playerAngle, Vector3f projectileDirection, float baseDamage) {
        angle = playerAngle;
        position = new Vector3f(playerPosition.x, playerPosition.y, playerPosition.z);
        direction = new Vector3f(projectileDirection.x, projectileDirection.y, projectileDirection.z).normalize();

        damage = baseDamage + (baseDamage * scalingFactor);

        float[] vertices = new float[]{
                -SIZE / 2.0f, -SIZE / 2.0f, 0.5f,
                -SIZE / 2.0f, SIZE / 2.0f, 0.5f,
                SIZE / 2.0f, SIZE / 2.0f, 0.5f,
                SIZE / 2.0f, -SIZE / 2.0f, 0.5f
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
        texture = new Texture("src/main/resources/images/projectile.png");
    }

    public void update() {
        position.x += direction.x * speed;
        position.y += direction.y * speed;
    }

    public void render() {
        Shader.PROJECTILE.enable();
        Shader.PROJECTILE.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(angle)));
        texture.bind();
        mesh.render();
        Shader.PROJECTILE.disable();
    }

    public Vector3f getMinBounds() {
        return new Vector3f(position.x - SIZE / 2, position.y - SIZE / 2, 0);
    }

    public Vector3f getMaxBounds() {
        return new Vector3f(position.x + SIZE / 2, position.y + SIZE / 2, 0);
    }
}
