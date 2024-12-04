package entities;

import graphic.*;
import map.Level;
import math.Matrix4f;
import math.Vector3f;
import org.joml.Vector2f;

public class DeadEnemy {
    public float SIZE = Level.TILE_SIZE / 2.0f;
    public VertexArray mesh;
    public Texture texture;
    public int angle;

    public Vector3f position;

    public Sprite sprite;
    public Vector2f[] texCoords;

    public DeadEnemy(Vector3f enemyPosition) {
        position = new Vector3f(enemyPosition.x, enemyPosition.y, enemyPosition.z);

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


        int width = 25;
        int height = 25;

        int offsetX = (64 - width) / 2;
        int offsetY = (64 - height) / 2;

        SpriteSheet spriteSheet = new SpriteSheet(
                new Texture("src/main/resources/slime.png"),
                64, 64, 24, 0
        );

        sprite = spriteSheet.getSpriteWithOffset(
                18, offsetX, offsetY,
                width, height
        );

        texture = sprite.getTexture();
        texCoords = sprite.getTexCoords();

        float[] tcs = new float[]{
                texCoords[3].x, texCoords[3].y, // Top-left
                texCoords[2].x, texCoords[2].y, // Bottom-left
                texCoords[1].x, texCoords[1].y, // Bottom-right
                texCoords[0].x, texCoords[0].y  // Top-right
        };

        mesh = new VertexArray(vertices, indices, tcs);
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
