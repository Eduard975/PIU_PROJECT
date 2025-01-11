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

    public float[] tcs;

    private SpriteSheet spriteSheet;
    private int animationFrame = 1;
    private int totalFrames;
    private long frameDuration = 100; // 100ms per frame
    private long lastFrameTime;
    private boolean animationComplete = false;

    float scaleX = 1f;  // Scale 50% of the original width
    float scaleY = 1f;  // Scale 50% of the original height

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

        // Load the sprite sheet with the death animation frames
        spriteSheet = new SpriteSheet(
                new Texture("src/main/resources/slime_death.png"),
                64, 64, 0 // Automatically calculate number of sprites
        );

        totalFrames = spriteSheet.getSpriteNum(); // Assume all frames in the sprite sheet are for the animation

        // Initialize first frame
        sprite = spriteSheet.getSpriteWithOffset(
                0, offsetX, offsetY, width, height
        );
        texture = sprite.getTexture();
        texCoords = sprite.getTexCoords();
        tcs = sprite.getTcs();

        mesh = new VertexArray(vertices, indices, tcs);
        lastFrameTime = System.currentTimeMillis();
    }

    private void updateSprite(int frameIndex, int offsetX, int offsetY, int width, int height) {
        sprite = spriteSheet.getSpriteWithOffset(
                frameIndex, offsetX, offsetY, width, height
        );
        texture = sprite.getTexture();
        texCoords = sprite.getTexCoords();
        tcs = sprite.getTcs();

        mesh.updateTexCoords(tcs);
    }

    public void update() {
        if (animationComplete) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDuration) {
            animationFrame++;
            lastFrameTime = currentTime;

            if (animationFrame >= totalFrames) {
                animationFrame = totalFrames - 1; // Keep last frame
                animationComplete = true;
            } else {
                // Update the sprite to the next frame
                if (animationFrame > 5) {
                    scaleY -= 0.1f;
                    updateSprite(animationFrame, (64 - 32) / 2, (64 - 20) / 2, 32, 20);
                } else
                    updateSprite(animationFrame, (64 - 25) / 2, (64 - 25) / 2, 25, 25);
            }
        }
    }

    public void render() {
        Shader.SLIME.enable();

        Shader.SLIME.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.scale(scaleX, scaleY, 1.0f)));
        Shader.SLIME.setUniform1i("isAlly", 0);

        texture.bind();
        mesh.render();
        Shader.SLIME.disable();
    }

    public boolean isAnimationComplete() {
        return animationComplete;
    }

    public Vector3f getMinBounds() {
        return new Vector3f(position.x - SIZE / 2, position.y - SIZE / 2, 0);
    }

    public Vector3f getMaxBounds() {
        return new Vector3f(position.x + SIZE / 2, position.y + SIZE / 2, 0);
    }
}
