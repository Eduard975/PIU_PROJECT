package map;

import graphic.Shader;
import graphic.VertexArray;
import math.Matrix4f;

public class Tile {
    private VertexArray mesh;
    private int type;
    private float[] color;

    public Tile(float x, float y, float size, int type) {
        this.type = type;

        float[] vertices = new float[]{
                x, y, 0.0f,
                x, y + size, 0.0f,
                x + size, y + size, 0.0f,
                x + size, y, 0.0f,
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0,
        };

        float[] texCoords = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        mesh = new VertexArray(vertices, indices, texCoords);

        switch (type) {
            case 0: color = new float[]{1.0f, 0.0f, 0.0f, 1.0f}; break; // Red
            case 1: color = new float[]{0.0f, 1.0f, 0.0f, 1.0f}; break; // Green
            default: color = new float[]{1.0f, 1.0f, 1.0f, 1.0f}; break; // White
        }
    }

    public void render() {
        mesh.render();
    }

    public int getType() {
        Shader.BACKGROUND.setUniformMat4f("tile_color", Matrix4f());
        return type;
    }
}
