package entities;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import map.Level;
import math.Matrix4f;
import math.Vector3f;

public class DeadEnemy {
    public float SIZE = Level.TILE_SIZE/2.0f;
    public VertexArray mesh;
    public Texture texture;
    public int angle;

    public Vector3f position;

    public DeadEnemy(Vector3f enemyPosition){
        position = new Vector3f(enemyPosition.x, enemyPosition.y, enemyPosition.z);

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

    public void render() {
        Shader.SLIME.enable();
        Shader.SLIME.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(90.0f)));
        texture.bind();
        mesh.render();
        Shader.SLIME.disable();
    }

    public Vector3f getMinBounds() {
        return new Vector3f(position.x - SIZE / 2, position.y - SIZE / 2, 0);
    }

    public Vector3f getMaxBounds() {
        return new Vector3f(position.x + SIZE / 2, position.y + SIZE / 2, 0);
    }
}
