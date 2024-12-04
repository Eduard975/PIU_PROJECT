package graphic;

import org.joml.Vector2f;

public class Sprite {

    private Texture texture;
    private Vector2f[] texCoords;

    float[] tcs;

    public Sprite(Texture texture) {
        this.texture = texture;
        Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        this.texCoords = texCoords;

        this.tcs = new float[]{
                texCoords[3].x, texCoords[3].y, // Top-left
                texCoords[2].x, texCoords[2].y, // Bottom-left
                texCoords[1].x, texCoords[1].y, // Bottom-right
                texCoords[0].x, texCoords[0].y  // Top-right
        };
    }

    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
        this.tcs = new float[]{
                texCoords[3].x, texCoords[3].y, // Top-left
                texCoords[2].x, texCoords[2].y, // Bottom-left
                texCoords[1].x, texCoords[1].y, // Bottom-right
                texCoords[0].x, texCoords[0].y  // Top-right
        };
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public float[] getTcs() {
        return this.tcs;
    }
}