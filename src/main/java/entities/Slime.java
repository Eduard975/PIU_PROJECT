package entities;

import graphic.Shader;
import graphic.Texture;
import graphic.VertexArray;
import math.Matrix4f;
import math.Vector3f;

public class Slime extends EnemyBase {

    public Slime(){
        attackDamage = 15;
        attackCooldown = 4000;
        position = new Vector3f();

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

    @Override
    public void update() {

    }

    @Override
    public void render() {
        Shader.SLIME.enable();
        Shader.SLIME.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(angle)));
        texture.bind();
        mesh.render();
        Shader.SLIME.disable();
    }
}
