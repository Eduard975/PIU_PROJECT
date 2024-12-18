package player;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;

public class Projectile {
    private float SIZE = Level.TILE_SIZE/8.0f;
    private VertexArray mesh;
    private Texture texture;

    private float angle;
    private Vector3f position;

    public float damage = 100;

    public Projectile(Vector3f playerPosition, float playerAngle) {
        angle = playerAngle;
        position = new Vector3f(playerPosition.x, playerPosition.y, playerPosition.z);

        float[] vertices = new float[] {
                -SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
                -SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
                 SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
                 SIZE / 2.0f, -SIZE / 2.0f, 0.2f
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("src/main/resources/projectile.png");
    }

    public void update(){
        position.x+=10;
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
