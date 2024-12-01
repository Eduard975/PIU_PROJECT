package player;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;

public class Projectile {
    private float SIZE = 4.0f;
    private VertexArray mesh;
    private Texture texture;

    private float angle;
    private Vector3f position;

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
        texture = new Texture("src/main/resources/player.png");
    }

    public void update(){
        position.x++;
    }

    public void render() {
        Shader.PROJECTILE.enable();
        Shader.PROJECTILE.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(angle)));
        texture.bind();
        mesh.render();
        Shader.PROJECTILE.disable();
    }
}
